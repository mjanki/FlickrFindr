package org.br.network.models

data class PhotosNetworkEntity(
        var page: Long,
        var pages: String,
        var photo: List<PhotoNetworkEntity>
)