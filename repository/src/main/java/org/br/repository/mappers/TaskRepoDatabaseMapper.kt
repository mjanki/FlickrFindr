package org.br.repository.mappers

import org.br.database.models.TaskDatabaseEntity
import org.br.repository.models.TaskRepoEntity
import org.br.util.interfaces.Mapper

class TaskRepoDatabaseMapper : Mapper<TaskRepoEntity, TaskDatabaseEntity> {
    override fun downstream(currentLayerEntity: TaskRepoEntity) = TaskDatabaseEntity(
            id = currentLayerEntity.id,
            uuid = currentLayerEntity.uuid,
            name = currentLayerEntity.name,
            date = currentLayerEntity.date,
            status = currentLayerEntity.status
    )

    override fun upstream(nextLayerEntity: TaskDatabaseEntity) = TaskRepoEntity(
            id = nextLayerEntity.id,
            uuid = nextLayerEntity.uuid,
            name = nextLayerEntity.name,
            date = nextLayerEntity.date,
            status = nextLayerEntity.status
    )
}