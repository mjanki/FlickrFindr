package org.br.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.br.database.daos.ErrorNetworkDatabaseDao
import org.br.database.daos.PhotoDatabaseDao
import org.br.database.daos.SearchTermDatabaseDao

import org.br.database.models.ErrorNetworkDatabaseEntity
import org.br.database.models.PhotoDatabaseEntity
import org.br.database.models.SearchTermDatabaseEntity
import org.br.database.type_converters.ErrorNetworkTypeConverter

@Database(entities = [
    PhotoDatabaseEntity::class,
    SearchTermDatabaseEntity::class,
    ErrorNetworkDatabaseEntity::class
], version = 1)

@TypeConverters(ErrorNetworkTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDatabaseDao
    abstract fun searchTermDao(): SearchTermDatabaseDao
    abstract fun errorNetworkDao(): ErrorNetworkDatabaseDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "flickrdb.db"
        ).build()

        fun destroyInstance() {
            instance = null
        }
    }
}