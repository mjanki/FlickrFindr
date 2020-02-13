package org.br.repository.mappers

import org.br.database.models.PhotoDatabaseEntity
import org.br.repository.models.PhotoRepoEntity
import org.br.util.interfaces.Mapper

class PhotoRepoDatabaseMapper : Mapper<PhotoRepoEntity, PhotoDatabaseEntity> {
    override fun downstream(currentLayerEntity: PhotoRepoEntity) = PhotoDatabaseEntity(
            id = currentLayerEntity.id,
            title = currentLayerEntity.title,
            imgThumb = currentLayerEntity.imgThumb,
            imgOriginal = currentLayerEntity.imgOriginal,
            isSaved = currentLayerEntity.isSaved
    )

    override fun upstream(nextLayerEntity: PhotoDatabaseEntity) = PhotoRepoEntity(
            id = nextLayerEntity.id,
            title = nextLayerEntity.title,
            imgThumb = nextLayerEntity.imgThumb,
            imgOriginal = nextLayerEntity.imgOriginal,
            isSaved = nextLayerEntity.isSaved
    )
}