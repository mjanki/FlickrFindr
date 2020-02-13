package org.br.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import org.br.database.models.PhotoDatabaseEntity

@Dao
interface PhotoDatabaseDao {
    @Query("SELECT * FROM photos")
    fun getAll(): Flowable<List<PhotoDatabaseEntity>>

    @Query("SELECT * FROM photos WHERE id = :id")
    fun getById(id: String): Flowable<List<PhotoDatabaseEntity>>

    // Insert OR Update
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(photoDatabaseEntity: PhotoDatabaseEntity): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(vararg photoDatabaseEntity: PhotoDatabaseEntity)

    @Query("DELETE FROM photos")
    fun deleteAll(): Completable

    @Query("DELETE FROM photos WHERE isSaved = ${false}")
    fun deleteNonSaved(): Completable
}