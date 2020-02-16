package org.br.repository.repositories

import android.content.Context
import io.reactivex.Flowable
import io.reactivex.rxkotlin.addTo
import org.br.database.daos.PhotoDatabaseDao
import org.br.database.daos.SearchTermDatabaseDao
import org.br.database.models.SearchTermDatabaseEntity
import org.br.network.daos.PhotosNetworkDao
import org.br.repository.mappers.ErrorNetworkRepoNetworkMapper
import org.br.repository.mappers.PhotoRepoDatabaseMapper
import org.br.repository.models.PhotoRepoEntity
import org.br.util.extensions.execute
import org.br.util.extensions.getValue

class PhotosRepository(private val ctx: Context? = null) : ErrorRepository(ctx) {
    // DAOs
    private lateinit var photoDatabaseDao: PhotoDatabaseDao
    private lateinit var searchTermsRepository: SearchTermsRepository
    private lateinit var photosNetworkDao: PhotosNetworkDao

    // Mappers
    private var photoRepoDatabaseMapper = PhotoRepoDatabaseMapper()
    private var errorNetworkRepoNetworkMapper = ErrorNetworkRepoNetworkMapper()

    override fun init() {
        super.init()

        init(
                testPhotoDatabaseDao = null,
                testSearchTermsRepository = null,
                testPhotosNetworkDao = null
        )
    }

    fun init(
            testPhotoDatabaseDao: PhotoDatabaseDao? = null,
            testSearchTermsRepository: SearchTermsRepository? = null,
            testPhotosNetworkDao: PhotosNetworkDao? = null) {

        photoDatabaseDao = testPhotoDatabaseDao ?: appDatabase.photoDao()
        photosNetworkDao = testPhotosNetworkDao ?: PhotosNetworkDao()

        searchTermsRepository = testSearchTermsRepository ?: SearchTermsRepository(ctx)
        searchTermsRepository.init()

        photosNetworkDao.errorNetwork.subscribe { errorNetworkEntity ->
            insertErrorNetwork(
                    errorNetworkRepoNetworkMapper.upstream(
                            errorNetworkEntity
                    )
            )
        }.addTo(disposables)

        photosNetworkDao.retrievedPhotos.subscribe {
            it.body()?.let { searchResultNetworkEntity ->
                insertPhotosInfo(
                        searchResultNetworkEntity.photos.photo.map { photoNetworkEntity ->
                            PhotoRepoEntity(
                                    photoNetworkEntity.id,
                                    photoNetworkEntity.title
                            )
                        }
                )

                searchResultNetworkEntity.photos.photo.forEach { photo ->
                    retrievePhotoSizes(photo.id)
                }
            }
        }.addTo(disposables)

        photosNetworkDao.retrievedPhotoSizes.subscribe { response ->
            response.body()?.let { photoSizesResultNetworkEntity ->
                // TODO: handle non existing images

                val thumbSize = photoSizesResultNetworkEntity.sizes.size.filter {
                    it.label == "Thumbnail"
                }[0]

                val originalSizeArray = photoSizesResultNetworkEntity.sizes.size.filter {
                    it.label == "Original" || it.label == "Large"
                }

                var originalSize = ""
                if (originalSizeArray.isNotEmpty()) {
                    originalSize = originalSizeArray[0].source
                }

                photoSizesResultNetworkEntity.photoId?.let { photoId ->
                    insertPhotoSizes(
                            PhotoRepoEntity(
                                    id = photoId,
                                    title = "",
                                    imgThumb = thumbSize.source,
                                    imgOriginal = originalSize
                            )
                    )
                }
            }
        }.addTo(disposables)
    }

    fun getPhotos(): Flowable<List<PhotoRepoEntity>> =
            photoDatabaseDao.getAll().flatMap { photoDatabaseEntityList ->
                Flowable.fromArray(
                        photoDatabaseEntityList.map {
                            photoRepoDatabaseMapper.upstream(it)
                        }
                )
            }

    fun getPhotoById(photoId: String): Flowable<List<PhotoRepoEntity>> =
        photoDatabaseDao.getById(photoId).flatMap { photoDatabaseEntities ->
            Flowable.fromArray(
                    photoDatabaseEntities.map {
                        photoRepoDatabaseMapper.upstream(it)
                    }
            )
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

    fun retrievePhotoSizes(photoId: String) {
        photosNetworkDao.retrievePhotoSize(photoId)
    }

    fun insertPhotosInfo(photos: List<PhotoRepoEntity>) {
        photoDatabaseDao.insert(
                *photos.map {
                    photoRepoDatabaseMapper.downstream(it)
                }.toTypedArray()
        )
    }

    // TODO: to be used when ready for photo sizes
    fun insertPhotoSizes(photo: PhotoRepoEntity) {
        photoDatabaseDao.getById(photo.id).getValue(
                onSuccess = {
                    var photoDatabaseEntity = photoRepoDatabaseMapper.downstream(photo)

                    if (it.isNotEmpty()) {
                        photoDatabaseEntity = it[0]

                        if (photo.title.isNotEmpty()) {
                            photoDatabaseEntity.title = photo.title
                        }

                        if (photo.imgThumb.isNotEmpty()) {
                            photoDatabaseEntity.imgThumb = photo.imgThumb
                        }

                        if (photo.imgOriginal.isNotEmpty()) {
                            photoDatabaseEntity.imgOriginal = photo.imgOriginal
                        }
                    }

                    photoDatabaseDao.update(photoDatabaseEntity).execute()
                }
        )
    }
}