package org.br.viewmodel.mappers

import org.br.repository.models.ErrorNetworkRepoEntity
import org.br.util.interfaces.Mapper
import org.br.viewmodel.models.ErrorNetworkViewModelEntity

class ErrorNetworkViewModelRepoMapper : Mapper<ErrorNetworkViewModelEntity, ErrorNetworkRepoEntity> {
    override fun downstream(currentLayerEntity: ErrorNetworkViewModelEntity) = ErrorNetworkRepoEntity(
            id = currentLayerEntity.id,
            type = currentLayerEntity.type,
            shouldPersist = currentLayerEntity.shouldPersist,
            code = currentLayerEntity.code,
            message = currentLayerEntity.message,
            action = currentLayerEntity.action
    )

    override fun upstream(nextLayerEntity: ErrorNetworkRepoEntity) = ErrorNetworkViewModelEntity(
            id = nextLayerEntity.id,
            type = nextLayerEntity.type,
            shouldPersist = nextLayerEntity.shouldPersist,
            code = nextLayerEntity.code,
            message = nextLayerEntity.message,
            action = nextLayerEntity.action
    )
}