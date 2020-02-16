package org.br.viewmodel.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxkotlin.addTo
import org.br.repository.repositories.PhotosRepository
import org.br.repository.repositories.SearchTermsRepository
import org.br.viewmodel.mappers.PhotoViewModelRepoMapper
import org.br.viewmodel.models.PhotoViewModelEntity

class PhotosListViewModel(application: Application) : BaseViewModel(application) {
    private lateinit var photosRepository: PhotosRepository
    private lateinit var searchTermsRepository: SearchTermsRepository

    private var allPhotos = MutableLiveData<List<PhotoViewModelEntity>>()
    private var allSearchTerms = MutableLiveData<List<String>>()

    private val photoViewModelRepoMapper = PhotoViewModelRepoMapper()

    private var currentPage = 1
    private var currentSearchTerm = ""

    fun init() {
        init(testPhotosRepository = null, testSearchTermsRepository = null)
    }

    fun init(testPhotosRepository: PhotosRepository? = null, testSearchTermsRepository: SearchTermsRepository? = null) {
        photosRepository = testPhotosRepository ?: PhotosRepository(getApplication())
        photosRepository.init()

        searchTermsRepository = testSearchTermsRepository ?: SearchTermsRepository(getApplication())
        searchTermsRepository.init()

        photosRepository.getPhotos().subscribe { photoRepoEntityList ->
            allPhotos.postValue(
                    photoRepoEntityList.map {
                        photoViewModelRepoMapper.upstream(it)
                    }
            )
        }.addTo(disposables)

        searchTermsRepository.getSearchTerms().subscribe { searchTerms ->
            allSearchTerms.postValue(searchTerms)
        }.addTo(disposables)

        /*taskRepository.isRetrievingTasks.subscribe {
            isRetrievingTasks.postValue(it)
        }.addTo(disposables)*/
    }

    fun getAllPhotos(): LiveData<List<PhotoViewModelEntity>> = allPhotos

    fun getAllSearchTerms(): LiveData<List<String>> = allSearchTerms

    /*private val isRetrievingTasks = MutableLiveData<Boolean>()
    fun getIsRetrievingTasks(): LiveData<Boolean> = isRetrievingTasks*/

    fun retrievePhotos(text: String, page: Long = 1) {
        currentPage = 1
        currentSearchTerm = text
        photosRepository.retrievePhotos(text, page)
    }

    fun retrieveNextPage() {
        photosRepository.retrievePhotos(currentSearchTerm, (++currentPage).toLong())
    }

    override fun onCleared() {
        super.onCleared()
        photosRepository.clearDisposables()
    }
}