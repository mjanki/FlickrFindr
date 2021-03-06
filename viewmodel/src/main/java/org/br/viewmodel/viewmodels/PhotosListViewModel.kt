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
    fun getAllPhotos(): LiveData<List<PhotoViewModelEntity>> = allPhotos

    private var allSearchTerms = MutableLiveData<List<String>>()
    fun getAllSearchTerms(): LiveData<List<String>> = allSearchTerms

    private val photoViewModelRepoMapper = PhotoViewModelRepoMapper()

    private var currentPage: Long = 1
    private var currentSearchTerm = ""

    fun init() {
        init(testPhotosRepository = null, testSearchTermsRepository = null)
    }

    fun init(testPhotosRepository: PhotosRepository? = null, testSearchTermsRepository: SearchTermsRepository? = null) {
        photosRepository = testPhotosRepository ?: PhotosRepository(getApplication())
        photosRepository.init()

        searchTermsRepository = testSearchTermsRepository ?: SearchTermsRepository(getApplication())
        searchTermsRepository.init()

        setupPhotosObservers()
        setupSearchTermsObservers()
    }

    private val isRetrievingPhotos = MutableLiveData<Boolean>()
    fun getIsRetrievingPhotos(): LiveData<Boolean> = isRetrievingPhotos

    private val noResults = MutableLiveData<Boolean>()
    fun getNoResults(): LiveData<Boolean> = noResults

    private fun setupPhotosObservers() {
        // Observe Photos and push to UI
        photosRepository.getPhotos().subscribe { photoRepoEntities ->
            allPhotos.postValue(
                    photoRepoEntities.map {
                        photoViewModelRepoMapper.upstream(it)
                    }
            )
        }.addTo(disposables)

        photosRepository.isRetrievingPhotos.subscribe {
            isRetrievingPhotos.postValue(it)
        }.addTo(disposables)

        photosRepository.noResults.subscribe {
            noResults.postValue(it)
        }.addTo(disposables)
    }

    private fun setupSearchTermsObservers() {
        // Observe Search Terms and push to UI
        searchTermsRepository.getSearchTerms().subscribe { searchTerms ->
            allSearchTerms.postValue(searchTerms)
        }.addTo(disposables)
    }

    fun retrievePhotosFirstPage(text: String) {
        currentPage = 1
        currentSearchTerm = text

        photosRepository.retrievePhotos(text, currentPage)
    }

    fun retrievePhotosNextPage() {
        if (currentSearchTerm.isNotEmpty()) {
            photosRepository.retrievePhotos(currentSearchTerm, (++currentPage))
        }
    }

    override fun onCleared() {
        super.onCleared()
        photosRepository.clearDisposables()
    }
}