package org.br.database.type_converters

import androidx.room.TypeConverter
import org.br.util.enums.ErrorNetworkTypes

object ErrorNetworkTypeConverter {
    @TypeConverter
    @JvmStatic
    fun toErrorNetworkType(value: String?): ErrorNetworkTypes? = value?.let {
        return when (it) {
            "TIMEOUT" -> ErrorNetworkTypes.TIMEOUT
            "IO" -> ErrorNetworkTypes.IO
            "HTTP" -> ErrorNetworkTypes.HTTP
            "OTHER" -> ErrorNetworkTypes.OTHER
            else -> ErrorNetworkTypes.OTHER
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromErrorNetworkType(errorNetworkType: ErrorNetworkTypes?) = when (errorNetworkType) {
        ErrorNetworkTypes.TIMEOUT -> "TIMEOUT"
        ErrorNetworkTypes.IO -> "IO"
        ErrorNetworkTypes.HTTP -> "HTTP"
        ErrorNetworkTypes.OTHER -> "OTHER"
        else -> "OTHER"
    }
}