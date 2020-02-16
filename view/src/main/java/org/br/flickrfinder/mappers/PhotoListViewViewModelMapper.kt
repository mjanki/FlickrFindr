package org.br.flickrfinder.mappers

import org.br.flickrfinder.models.PhotoListViewEntity
import org.br.util.interfaces.Mapper
import org.br.viewmodel.models.PhotoViewModelEntity

class PhotoListViewViewModelMapper : Mapper<PhotoListViewEntity, PhotoViewModelEntity> {
    override fun downstream(currentLayerEntity: PhotoListViewEntity) = PhotoViewModelEntity(
            id = currentLayerEntity.id,
            title = currentLayerEntity.title,
            isSaved = currentLayerEntity.isSaved,
            imgThumb = currentLayerEntity.imgThumb
    )

    override fun upstream(nextLayerEntity: PhotoViewModelEntity) = PhotoListViewEntity(
            id = nextLayerEntity.id,
            title = nextLayerEntity.title,
            isSaved = nextLayerEntity.isSaved,
            imgThumb = nextLayerEntity.imgThumb
    )
}