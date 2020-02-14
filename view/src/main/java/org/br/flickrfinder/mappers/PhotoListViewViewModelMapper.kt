package org.br.flickrfinder.mappers

import org.br.flickrfinder.models.PhotoListViewEntity
import org.br.util.interfaces.Mapper
import org.br.viewmodel.models.PhotoViewModelEntity

class PhotoListViewViewModelMapper : Mapper<PhotoListViewEntity, PhotoViewModelEntity> {
    override fun downstream(currentLayerEntity: PhotoListViewEntity) = PhotoViewModelEntity(
            id = "",
            title = currentLayerEntity.title,
            imgThumb = currentLayerEntity.imgThumb
    )

    override fun upstream(nextLayerEntity: PhotoViewModelEntity) = PhotoListViewEntity(
            title = nextLayerEntity.title,
            imgThumb = nextLayerEntity.imgThumb
    )
}