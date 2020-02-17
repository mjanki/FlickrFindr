package org.br.viewmodel.models

import android.graphics.Bitmap

data class PhotoViewModelEntity(
        var id: String,
        var title: String,
        var thumbUrl: String = "",
        var originalUrl: String = "",
        var thumbBitmap: Bitmap? = null,
        var originalBitmap: Bitmap? = null
)