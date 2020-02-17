package org.br.flickrfinder.models

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoViewEntity(
        var id: String,
        var title: String,
        var thumbUrl: String = "",
        var originalUrl: String = "",
        var thumbBitmap: Bitmap? = null,
        var originalBitmap: Bitmap? = null
): Parcelable