package org.br.viewmodel.mappers

import org.br.repository.models.PhotoRepoEntity
import org.br.util.interfaces.Mapper
import org.br.viewmodel.models.PhotoViewModelEntity

class PhotoViewModelRepoMapper : Mapper<PhotoViewModelEntity, PhotoRepoEntity> {
    override fun downstream(currentLayerEntity: PhotoViewModelEntity) = PhotoRepoEntity(
            id = currentLayerEntity.id,
            title = currentLayerEntity.title,
            thumbUrl = currentLayerEntity.thumbUrl,
            originalUrl = currentLayerEntity.originalUrl,
            thumbBitmap = currentLayerEntity.thumbBitmap,
            originalBitmap = currentLayerEntity.originalBitmap
    )

    override fun upstream(nextLayerEntity: PhotoRepoEntity) = PhotoViewModelEntity(
            id = nextLayerEntity.id,
            title = nextLayerEntity.title,
            thumbUrl = nextLayerEntity.thumbUrl,
            originalUrl = nextLayerEntity.originalUrl,
            thumbBitmap = nextLayerEntity.thumbBitmap,
            originalBitmap = nextLayerEntity.originalBitmap
    )
}