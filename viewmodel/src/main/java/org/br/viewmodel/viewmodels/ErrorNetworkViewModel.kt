package org.br.viewmodel.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxkotlin.addTo
import org.br.repository.repositories.ErrorRepository
import org.br.viewmodel.mappers.ErrorNetworkViewModelRepoMapper
import org.br.viewmodel.models.ErrorNetworkViewModelEntity

class ErrorNetworkViewModel(application: Application) : BaseViewModel(application) {
    private lateinit var errorRepository: ErrorRepository

    private var errorsNetwork = MutableLiveData<List<ErrorNetworkViewModelEntity>>()
    fun getErrorsNetwork(): LiveData<List<ErrorNetworkViewModelEntity>> = errorsNetwork

    private var errorNetworkRepoViewModelMapper = ErrorNetworkViewModelRepoMapper()

    fun init() {
        init(testErrorRepository = null)
    }

    fun init(testErrorRepository: ErrorRepository? = null) {
        errorRepository = testErrorRepository ?: ErrorRepository(getApplication())
        errorRepository.init()

        errorRepository.getErrorsNetwork().subscribe { errorNetworkRepoEntityList ->
            errorsNetwork.postValue(
                    errorNetworkRepoEntityList.map { errorNetworkRepoEntity ->
                        errorNetworkRepoViewModelMapper.upstream(errorNetworkRepoEntity)
                    }
            )
        }.addTo(disposables)
    }

    fun deleteErrorNetwork(errorNetworkViewModelEntity: ErrorNetworkViewModelEntity) {
        errorRepository.deleteErrorNetwork(
                errorNetworkRepoViewModelMapper.downstream(
                        errorNetworkViewModelEntity
                )
        )
    }
}