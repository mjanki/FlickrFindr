package org.br.viewmodel.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxkotlin.addTo
import org.br.repository.repositories.PhotosRepository
import org.br.viewmodel.mappers.PhotoViewModelRepoMapper
import org.br.viewmodel.models.PhotoViewModelEntity

class PhotoViewModel(application: Application) : BaseViewModel(application) {
    private lateinit var photosRepository: PhotosRepository
    private var photo = MutableLiveData<PhotoViewModelEntity>()

    private val photoViewModelRepoMapper = PhotoViewModelRepoMapper()

    fun init(photoId: String) {
        init(photoId = photoId, testPhotosRepository = null)
    }

    fun init(photoId: String, testPhotosRepository: PhotosRepository? = null) {
        photosRepository = testPhotosRepository ?: PhotosRepository(getApplication())
        photosRepository.init()

        photosRepository.getPhotoById(photoId).subscribe { photoRepoEntityList ->
            // TODO: Handle empty result
            if (photoRepoEntityList.isEmpty()) { return@subscribe }

            val photoRepoEntity = photoRepoEntityList[0]
            photo.postValue(photoViewModelRepoMapper.upstream(photoRepoEntity))
        }.addTo(disposables)

        /*taskRepository.isRetrievingTasks.subscribe {
            isRetrievingTasks.postValue(it)
        }.addTo(disposables)*/
    }

    fun getPhoto(): LiveData<PhotoViewModelEntity> = photo

    override fun onCleared() {
        super.onCleared()
        photosRepository.clearDisposables()
    }
}