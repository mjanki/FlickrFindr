package org.br.repository.repositories

import android.content.Context
import android.graphics.Bitmap
import io.reactivex.Flowable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import org.br.database.daos.PhotoDatabaseDao
import org.br.network.daos.PhotosNetworkDao
import org.br.repository.mappers.ErrorNetworkRepoNetworkMapper
import org.br.repository.mappers.PhotoRepoDatabaseMapper
import org.br.repository.models.PhotoRepoEntity
import org.br.storage.PhotoStorageDao
import org.br.util.extensions.execute
import org.br.util.extensions.getValue

class PhotosRepository(private val ctx: Context? = null) : ErrorRepository(ctx) {
    // DAOs
    private lateinit var photoDatabaseDao: PhotoDatabaseDao
    private lateinit var searchTermsRepository: SearchTermsRepository
    private lateinit var photosNetworkDao: PhotosNetworkDao
    private lateinit var photoStorageDao: PhotoStorageDao

    // Mappers
    private var photoRepoDatabaseMapper = PhotoRepoDatabaseMapper()
    private var errorNetworkRepoNetworkMapper = ErrorNetworkRepoNetworkMapper()

    override fun init() {
        super.init()

        init(
                testPhotoDatabaseDao = null,
                testSearchTermsRepository = null,
                testPhotosNetworkDao = null,
                testPhotoStorageDao = null
        )
    }

    fun init(
            testPhotoDatabaseDao: PhotoDatabaseDao? = null,
            testSearchTermsRepository: SearchTermsRepository? = null,
            testPhotosNetworkDao: PhotosNetworkDao? = null,
            testPhotoStorageDao: PhotoStorageDao? = null) {

        photoDatabaseDao = testPhotoDatabaseDao ?: appDatabase.photoDao()
        photosNetworkDao = testPhotosNetworkDao ?: PhotosNetworkDao()
        photoStorageDao = testPhotoStorageDao ?: PhotoStorageDao(ctx!!)

        searchTermsRepository = testSearchTermsRepository ?: SearchTermsRepository(ctx)
        searchTermsRepository.init()

        setupErrorNetwork()
        setupPhotosObservers()
        setupPhotoSizesObservers()
    }

    // region Setup

    private fun setupErrorNetwork() {
        photosNetworkDao.errorNetwork.subscribe { errorNetworkEntity ->
            insertErrorNetwork(
                    errorNetworkRepoNetworkMapper.upstream(
                            errorNetworkEntity
                    )
            )
        }.addTo(disposables)
    }

    // Used to notify UI of retrieving photos status
    lateinit var isRetrievingPhotos: PublishSubject<Boolean>
    var noResults: PublishSubject<Boolean> = PublishSubject.create()

    private fun setupPhotosObservers() {
        // Observe retrieved photos from the Network and save initial info in the DB, then fetch
        // photo sizes from the Network for each photo
        photosNetworkDao.retrievedPhotos.subscribe { response ->
            response.body()?.let { searchResultNetworkEntity ->
                searchResultNetworkEntity.photos?.let { photos ->
                    // If no results found notify VM and exit subscribe
                    if (photos.photo.isEmpty()) {
                        noResults.onNext(true)
                        return@subscribe
                    }

                    // Insert initial info if they don't exist in the DB
                    photoDatabaseDao.insert(
                            *photos.photo.map { photoNetworkEntity ->
                                PhotoRepoEntity(
                                        photoNetworkEntity.id,
                                        photoNetworkEntity.title
                                )
                            }.map {
                                photoRepoDatabaseMapper.downstream(it)
                            }.toTypedArray()
                    )

                    // Retrieve Photo Sizes for each photo
                    photos.photo.forEach { photo ->
                        retrievePhotoSizes(photo.id)
                    }
                }
            }
        }.addTo(disposables)

        isRetrievingPhotos = photosNetworkDao.isRetrievingPhotos
    }

    private fun setupPhotoSizesObservers() {
        // Observe photo sizes retrieved from the Network and update DB entries accordingly
        photosNetworkDao.retrievedPhotoSizes.subscribe { response ->
            response.body()?.let { photoSizesResultNetworkEntity ->
                // Get Thumbnail info if exists
                val thumbSizeList = photoSizesResultNetworkEntity.sizes?.size?.filter {
                    it.label == "Thumbnail"
                }

                var thumbSizeUrl = ""
                if (thumbSizeList?.isNotEmpty() == true) {
                    thumbSizeUrl = thumbSizeList.first().source
                }

                // Get Original (or Large) info if exists
                val originalSizeList = photoSizesResultNetworkEntity.sizes?.size?.filter {
                    it.label == "Original" || it.label == "Large"
                }

                var originalSizeUrl = ""
                if (originalSizeList?.isNotEmpty() == true) {
                    originalSizeUrl = originalSizeList.first().source
                }

                // Update DB if both Thumbnail and Original/Large exist
                if (thumbSizeUrl.isNotEmpty() && originalSizeUrl.isNotEmpty()) {
                    photoSizesResultNetworkEntity.photoId?.let { photoId ->
                        insertPhotoSizes(
                                PhotoRepoEntity(
                                        id = photoId,
                                        title = "",
                                        thumbUrl = thumbSizeUrl,
                                        originalUrl = originalSizeUrl
                                )
                        )
                    }
                }
            }
        }.addTo(disposables)
    }

    // endregion Setup

    // region Photos List

    fun getPhotos(): Flowable<List<PhotoRepoEntity>> =
            photoDatabaseDao.getAll().flatMap { photoDatabaseEntities ->
                val photoRepoEntities = photoDatabaseEntities.map {
                    photoRepoDatabaseMapper.upstream(it)
                }

                // Add Bitmap if exists in Storage
                photoRepoEntities.forEach {
                    it.thumbBitmap = photoStorageDao.readPhotoBitmap(it.thumbPath)
                    it.originalBitmap = photoStorageDao.readPhotoBitmap(it.originalPath)
                }

                Flowable.fromArray(photoRepoEntities)
            }

    fun retrievePhotos(text: String, page: Long = 1) {
        if (page == 1.toLong()) {
            // Delete all non-saved entites on new searches
            photoDatabaseDao.deleteNonSaved().execute()

            // Add search term to SearchTerms Table
            searchTermsRepository.saveSearchTerm(text)
        }

        photosNetworkDao.retrievePhotos(text, page)
    }

    private fun retrievePhotoSizes(photoId: String) {
        // Check if already exists in the DB
        photoDatabaseDao.getById(photoId).getValue(
                onSuccess = { photoDatabaseEntities ->
                    var shouldRetrieve = true

                    if (photoDatabaseEntities.isNotEmpty()) {
                        val photoDatabaseEntity = photoDatabaseEntities[0]

                        val (_, _, tUrl, oUrl, tPath, oPath) = photoDatabaseEntity
                        if (tUrl.isNotEmpty() || oUrl.isNotEmpty() || tPath.isNotEmpty() || oPath.isNotEmpty()) {
                            shouldRetrieve = false
                        }
                    }

                    // Only retrieve if doesn't exist in DB
                    if (shouldRetrieve) {
                        photosNetworkDao.retrievePhotoSize(photoId)
                    }
                }
        )
    }


    private fun insertPhotoSizes(photo: PhotoRepoEntity) {
        photoDatabaseDao.getById(photo.id).getValue(
                onSuccess = {
                    // Inserting sizes requires the photo info to exist in the DB
                    if (it.isNotEmpty()) {
                        val photoDatabaseEntity = it.first()

                        if (photo.thumbUrl.isNotEmpty()) {
                            photoDatabaseEntity.thumbUrl = photo.thumbUrl
                        }

                        if (photo.originalUrl.isNotEmpty()) {
                            photoDatabaseEntity.originalUrl = photo.originalUrl
                        }

                        photoDatabaseDao.update(photoDatabaseEntity).execute()
                    }
                }
        )
    }

    // endregion Photos List

    // region Single Photo

    val photoSaved = PublishSubject.create<Boolean>()

    fun savePhoto(photoId: String, thumbBitmap: Bitmap, originalBitmap: Bitmap) {
        // Save both Thumbnail and Original in Storage
        val thumbPath = photoStorageDao.saveThumbBitmap(photoId, thumbBitmap)
        val originalPath = photoStorageDao.saveThumbBitmap(photoId, originalBitmap)

        // Update DB with new Paths
        photoDatabaseDao.getById(photoId).getValue(
                onSuccess = {
                    if (it.isNotEmpty()) {
                        val photo = it.first()

                        photo.thumbPath = thumbPath
                        photo.originalPath = originalPath

                        photoDatabaseDao.update(photo).execute(
                                onSuccess = {
                                    photoSaved.onNext(true)
                                }
                        )
                    }
                }
        )
    }

    // endregion Single Photo



}