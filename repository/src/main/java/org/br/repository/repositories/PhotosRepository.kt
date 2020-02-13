package org.br.repository.repositories

import android.content.Context
import io.reactivex.Flowable
import io.reactivex.rxkotlin.addTo
import org.br.database.daos.PhotoDatabaseDao
import org.br.network.daos.PhotosNetworkDao
import org.br.repository.mappers.ErrorNetworkRepoNetworkMapper
import org.br.repository.mappers.PhotoRepoDatabaseMapper
import org.br.repository.models.PhotoRepoEntity
import org.br.util.extensions.execute
import org.br.util.extensions.getValue

class PhotosRepository(ctx: Context? = null) : ErrorRepository(ctx) {
    // DAOs
    private lateinit var photoDatabaseDao: PhotoDatabaseDao
    private lateinit var photosNetworkDao: PhotosNetworkDao

    // Mappers
    private var photoRepoDatabaseMapper = PhotoRepoDatabaseMapper()
    private var errorNetworkRepoNetworkMapper = ErrorNetworkRepoNetworkMapper()

    override fun init() {
        super.init()

        init(testPhotoDatabaseDao = null, testPhotosNetworkDao = null)
    }

    fun init(testPhotoDatabaseDao: PhotoDatabaseDao? = null, testPhotosNetworkDao: PhotosNetworkDao? = null) {
        photoDatabaseDao = testPhotoDatabaseDao ?: appDatabase.photoDao()
        photosNetworkDao = testPhotosNetworkDao ?: PhotosNetworkDao()

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
            }
        }.addTo(disposables)
    }

    fun getPhotos(): Flowable<List<String>> =
            photoDatabaseDao.getAll().flatMap { photoDatabaseEntityList ->
                Flowable.fromArray(
                        photoDatabaseEntityList.map {
                            it.title
                        }
                )
            }

    fun retrievePhotos(text: String, page: Long = 1) {
        photosNetworkDao.retrievePhotos(text, page)
    }

    fun insertPhotosInfo(photos: List<PhotoRepoEntity>) {
        photoDatabaseDao.upsert(
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

                    photoDatabaseDao.upsert(photoDatabaseEntity).execute()
                }
        )
    }
}