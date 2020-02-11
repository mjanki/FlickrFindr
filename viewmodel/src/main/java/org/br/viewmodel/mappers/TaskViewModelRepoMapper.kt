package org.br.viewmodel.mappers

import org.br.repository.models.TaskRepoEntity
import org.br.util.interfaces.Mapper
import org.br.viewmodel.models.TaskViewModelEntity

class TaskViewModelRepoMapper : Mapper<TaskViewModelEntity, TaskRepoEntity> {
    override fun downstream(currentLayerEntity: TaskViewModelEntity) = TaskRepoEntity(
            id = currentLayerEntity.id,
            name = currentLayerEntity.name,
            date = currentLayerEntity.date,
            status = currentLayerEntity.status
    )

    override fun upstream(nextLayerEntity: TaskRepoEntity) = TaskViewModelEntity(
            id = nextLayerEntity.id,
            name = nextLayerEntity.name,
            date = nextLayerEntity.date,
            status = nextLayerEntity.status
    )
}