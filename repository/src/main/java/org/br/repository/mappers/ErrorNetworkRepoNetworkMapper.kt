package org.br.repository.mappers

import org.br.network.models.ErrorNetworkEntity
import org.br.repository.models.ErrorNetworkRepoEntity
import org.br.util.interfaces.Mapper

class ErrorNetworkRepoNetworkMapper : Mapper<ErrorNetworkRepoEntity, ErrorNetworkEntity> {
    override fun downstream(currentLayerEntity: ErrorNetworkRepoEntity) = ErrorNetworkEntity(
            type = currentLayerEntity.type,
            shouldPersist = currentLayerEntity.shouldPersist,
            code = currentLayerEntity.code,
            message = currentLayerEntity.message,
            action = currentLayerEntity.action
    )

    override fun upstream(nextLayerEntity: ErrorNetworkEntity) = ErrorNetworkRepoEntity(
            type = nextLayerEntity.type,
            shouldPersist = nextLayerEntity.shouldPersist,
            code = nextLayerEntity.code,
            message = nextLayerEntity.message,
            action = nextLayerEntity.action
    )
}