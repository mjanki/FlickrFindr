package org.br.repository.models

import android.graphics.Bitmap

data class PhotoRepoEntity(
        var id: String,
        var title: String,
        var thumbUrl: String = "",
        var originalUrl: String = "",
        var thumbPath: String = "",
        var originalPath: String = "",
        var thumbBitmap: Bitmap? = null,
        var originalBitmap: Bitmap? = null
)