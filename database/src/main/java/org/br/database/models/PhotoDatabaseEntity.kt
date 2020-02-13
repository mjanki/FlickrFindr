package org.br.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoDatabaseEntity(
        @PrimaryKey var id: String,
        @ColumnInfo(name = "title") var title: String,
        @ColumnInfo(name = "imgThumb") var imgThumb: String,
        @ColumnInfo(name = "imgOriginal") var imgOriginal: String,
        @ColumnInfo(name = "isSaved") var isSaved: Boolean = false
)