package org.br.repository.mappers

import org.threeten.bp.OffsetDateTime
import org.br.network.models.TaskNetworkEntity
import org.br.repository.models.TaskRepoEntity
import org.br.util.interfaces.Mapper

class TaskRepoNetworkMapper : Mapper<TaskRepoEntity, TaskNetworkEntity> {
    override fun downstream(currentLayerEntity: TaskRepoEntity) = TaskNetworkEntity(
            uuid = currentLayerEntity.uuid,
            name = currentLayerEntity.name,
            date = "${currentLayerEntity.date}",
            status = currentLayerEntity.status
    )

    override fun upstream(nextLayerEntity: TaskNetworkEntity) = TaskRepoEntity(
            uuid = nextLayerEntity.uuid,
            name = nextLayerEntity.name,
            date = OffsetDateTime.parse(nextLayerEntity.date),
            status = nextLayerEntity.status
    )
}