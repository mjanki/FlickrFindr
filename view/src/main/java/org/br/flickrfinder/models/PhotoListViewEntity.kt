package org.br.flickrfinder.models

data class PhotoListViewEntity(
        var id: String,
        var title: String,
        var isSaved: Boolean,
        var imgThumb: String = ""
)