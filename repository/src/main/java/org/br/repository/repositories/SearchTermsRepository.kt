package org.br.repository.repositories

import android.content.Context
import io.reactivex.Flowable
import org.br.database.daos.SearchTermDatabaseDao
import org.br.database.models.SearchTermDatabaseEntity
import org.br.util.extensions.execute

class SearchTermsRepository(ctx: Context? = null) : ErrorRepository(ctx) {
    // DAOs
    private lateinit var searchTermDatabaseDao: SearchTermDatabaseDao

    override fun init() {
        super.init()

        init(testSearchTermDatabaseDao = null)
    }

    fun init(testSearchTermDatabaseDao: SearchTermDatabaseDao? = null) {
        searchTermDatabaseDao = testSearchTermDatabaseDao ?: appDatabase.searchTermDao()
    }

    fun saveSearchTerm(searchTerm: String) {
        searchTermDatabaseDao.upsert(SearchTermDatabaseEntity(searchTerm)).execute()
    }

    fun getSearchTerms(): Flowable<List<String>> =
            searchTermDatabaseDao.getAll().flatMap { searchTermDatabaseEntities ->
                Flowable.fromArray(
                        searchTermDatabaseEntities.map {
                            it.text
                        }
                )
            }
}