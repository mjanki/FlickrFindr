package org.br.viewmodel.mappers

import org.br.repository.models.PhotoRepoEntity
import org.br.util.interfaces.Mapper
import org.br.viewmodel.models.PhotoViewModelEntity

class PhotoViewModelRepoMapper : Mapper<PhotoViewModelEntity, PhotoRepoEntity> {
    override fun downstream(currentLayerEntity: PhotoViewModelEntity) = PhotoRepoEntity(
            id = currentLayerEntity.id,
            title = currentLayerEntity.title,
            imgThumb = currentLayerEntity.imgThumb,
            imgOriginal = currentLayerEntity.imgOriginal,
            isSaved = currentLayerEntity.isSaved
    )

    override fun upstream(nextLayerEntity: PhotoRepoEntity) = PhotoViewModelEntity(
            id = nextLayerEntity.id,
            title = nextLayerEntity.title,
            imgThumb = nextLayerEntity.imgThumb,
            imgOriginal = nextLayerEntity.imgOriginal,
            isSaved = nextLayerEntity.isSaved
    )
}