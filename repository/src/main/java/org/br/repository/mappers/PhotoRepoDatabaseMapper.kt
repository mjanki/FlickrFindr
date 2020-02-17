package org.br.repository.mappers

import org.br.database.models.PhotoDatabaseEntity
import org.br.repository.models.PhotoRepoEntity
import org.br.util.interfaces.Mapper

class PhotoRepoDatabaseMapper : Mapper<PhotoRepoEntity, PhotoDatabaseEntity> {
    override fun downstream(currentLayerEntity: PhotoRepoEntity) = PhotoDatabaseEntity(
            id = currentLayerEntity.id,
            title = currentLayerEntity.title,
            thumbUrl = currentLayerEntity.thumbUrl,
            originalUrl = currentLayerEntity.originalUrl,
            thumbPath = currentLayerEntity.thumbPath,
            originalPath = currentLayerEntity.originalPath
    )

    override fun upstream(nextLayerEntity: PhotoDatabaseEntity) = PhotoRepoEntity(
            id = nextLayerEntity.id,
            title = nextLayerEntity.title,
            thumbUrl = nextLayerEntity.thumbUrl,
            originalUrl = nextLayerEntity.originalUrl,
            thumbPath = nextLayerEntity.thumbPath,
            originalPath = nextLayerEntity.originalPath
    )
}