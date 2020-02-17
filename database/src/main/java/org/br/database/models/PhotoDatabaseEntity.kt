package org.br.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoDatabaseEntity(
        @PrimaryKey var id: String,
        @ColumnInfo(name = "title") var title: String,
        @ColumnInfo(name = "thumbUrl") var thumbUrl: String,
        @ColumnInfo(name = "originalUrl") var originalUrl: String,
        @ColumnInfo(name = "thumbPath") var thumbPath: String = "",
        @ColumnInfo(name = "originalPath") var originalPath: String = ""
)