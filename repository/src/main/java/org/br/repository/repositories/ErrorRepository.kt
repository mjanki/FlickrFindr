package org.br.repository.repositories

import android.content.Context
import io.reactivex.Flowable
import org.br.database.daos.ErrorNetworkDatabaseDao
import org.br.database.models.ErrorNetworkDatabaseEntity
import org.br.repository.mappers.ErrorNetworkRepoDatabaseMapper
import org.br.repository.models.ErrorNetworkRepoEntity
import org.br.util.extensions.execute

open class ErrorRepository(ctx: Context? = null): Repository(ctx) {
    // DAOs
    private lateinit var errorNetworkDatabaseDao: ErrorNetworkDatabaseDao

    // Observables
    private lateinit var allErrorsNetwork: Flowable<List<ErrorNetworkDatabaseEntity>>

    // Mappers
    private var errorNetworkRepoDatabaseMapper = ErrorNetworkRepoDatabaseMapper()

    override fun init() {
        super.init()

        init(testErrorNetworkDatabaseDao = null)
    }

    fun init(testErrorNetworkDatabaseDao: ErrorNetworkDatabaseDao? = null) {
        errorNetworkDatabaseDao = testErrorNetworkDatabaseDao ?: appDatabase.errorNetworkDao()
        allErrorsNetwork = errorNetworkDatabaseDao.getAll()
    }

    fun getErrorsNetwork(): Flowable<List<ErrorNetworkRepoEntity>> {
        return allErrorsNetwork.flatMap { errorNetworkDatabaseEntityList ->
            Flowable.fromArray(
                    errorNetworkDatabaseEntityList.map { errorNetworkDatabaseEntity ->
                        errorNetworkRepoDatabaseMapper.upstream(errorNetworkDatabaseEntity)
                    }
            )
        }
    }

    fun deleteErrorNetwork(errorNetworkRepoEntity: ErrorNetworkRepoEntity) {
        errorNetworkDatabaseDao.delete(
                errorNetworkRepoDatabaseMapper.downstream(
                        errorNetworkRepoEntity
                )
        ).execute()
    }

    fun insertErrorNetwork(errorNetworkRepoEntity: ErrorNetworkRepoEntity) {
        errorNetworkDatabaseDao.insert(
                errorNetworkRepoDatabaseMapper.downstream(errorNetworkRepoEntity)
        ).execute()
    }
}