package org.br.flickrfinder.models

import android.graphics.Bitmap

data class PhotoViewEntity(
        var id: String,
        var title: String,
        var thumbUrl: String = "",
        var originalUrl: String = "",
        var thumbBitmap: Bitmap? = null,
        var originalBitmap: Bitmap? = null
)