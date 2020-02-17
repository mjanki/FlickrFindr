package org.br.flickrfinder.mappers

import org.br.flickrfinder.models.PhotoViewEntity
import org.br.util.interfaces.Mapper
import org.br.viewmodel.models.PhotoViewModelEntity

class PhotoViewViewModelMapper : Mapper<PhotoViewEntity, PhotoViewModelEntity> {
    override fun downstream(currentLayerEntity: PhotoViewEntity) = PhotoViewModelEntity(
            id = currentLayerEntity.id,
            title = currentLayerEntity.title,
            thumbUrl = currentLayerEntity.thumbUrl,
            originalUrl = currentLayerEntity.originalUrl,
            thumbBitmap = currentLayerEntity.thumbBitmap,
            originalBitmap = currentLayerEntity.originalBitmap
    )

    override fun upstream(nextLayerEntity: PhotoViewModelEntity) = PhotoViewEntity(
            id = nextLayerEntity.id,
            title = nextLayerEntity.title,
            thumbUrl = nextLayerEntity.thumbUrl,
            originalUrl = nextLayerEntity.originalUrl,
            thumbBitmap = nextLayerEntity.thumbBitmap,
            originalBitmap = nextLayerEntity.originalBitmap
    )
}