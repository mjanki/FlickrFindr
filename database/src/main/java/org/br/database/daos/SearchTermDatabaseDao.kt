package org.br.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable
import io.reactivex.Single
import org.br.database.models.SearchTermDatabaseEntity

@Dao
interface SearchTermDatabaseDao {
    @Query("SELECT * FROM search_terms")
    fun getAll(): Flowable<List<SearchTermDatabaseEntity>>

    // Insert OR Update
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(searchTermDatabaseEntity: SearchTermDatabaseEntity): Single<Long>
}