package org.br.repository.models

data class PhotoRepoEntity(
        var id: String,
        var title: String,
        var imgThumb: String = "",
        var imgOriginal: String = "",
        var isSaved: Boolean = false
)