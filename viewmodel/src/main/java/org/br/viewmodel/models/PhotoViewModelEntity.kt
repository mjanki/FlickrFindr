package org.br.viewmodel.models

data class PhotoViewModelEntity(
        var id: String,
        var title: String,
        var imgThumb: String = "",
        var imgOriginal: String = "",
        var isSaved: Boolean = false
)