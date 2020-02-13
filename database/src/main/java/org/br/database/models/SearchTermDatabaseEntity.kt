package org.br.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_terms")
data class SearchTermDatabaseEntity(
        @PrimaryKey @ColumnInfo(name = "text") var text: String
)