package org.br.viewmodel.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxkotlin.addTo
import org.br.repository.repositories.PhotosRepository

class PhotosListViewModel(application: Application) : BaseViewModel(application) {
    private lateinit var photosRepository: PhotosRepository
    private var allPhotos = MutableLiveData<List<String>>()

    fun init() {
        init(testPhotosRepository = null)
    }

    fun init(testPhotosRepository: PhotosRepository? = null) {
        photosRepository = testPhotosRepository ?: PhotosRepository(getApplication())
        photosRepository.init()

        photosRepository.getPhotos().subscribe { photoRepoEntityList ->
            allPhotos.postValue(photoRepoEntityList)
        }.addTo(disposables)

        /*taskRepository.isRetrievingTasks.subscribe {
            isRetrievingTasks.postValue(it)
        }.addTo(disposables)*/
    }

    fun getAllPhotos(): LiveData<List<String>> = allPhotos

    /*private val isRetrievingTasks = MutableLiveData<Boolean>()
    fun getIsRetrievingTasks(): LiveData<Boolean> = isRetrievingTasks*/

    fun retrievePhotos(text: String, page: Long = 1) {
        photosRepository.retrievePhotos(text, page)
    }

    override fun onCleared() {
        super.onCleared()
        photosRepository.clearDisposables()
    }
}