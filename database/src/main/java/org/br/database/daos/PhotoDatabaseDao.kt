package org.br.database.daos

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import org.br.database.models.PhotoDatabaseEntity

@Dao
interface PhotoDatabaseDao {
    @Query("SELECT * FROM photos")
    fun getAll(): Flowable<List<PhotoDatabaseEntity>>

    @Query("SELECT * FROM photos WHERE id = :id")
    fun getById(id: String): Flowable<List<PhotoDatabaseEntity>>

    @Update
    fun update(photoDatabaseEntity: PhotoDatabaseEntity): Completable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg photoDatabaseEntity: PhotoDatabaseEntity)

    @Query("DELETE FROM photos")
    fun deleteAll(): Completable

    @Query("DELETE FROM photos WHERE isSaved = ${false}")
    fun deleteNonSaved(): Completable
}