package org.br.flickrfinder.mappers

import org.br.flickrfinder.models.TaskViewEntity
import org.br.util.interfaces.Mapper
import org.br.viewmodel.models.TaskViewModelEntity

class TaskViewViewModelMapper : Mapper<TaskViewEntity, TaskViewModelEntity> {
    override fun downstream(currentLayerEntity: TaskViewEntity) = TaskViewModelEntity(
            id = currentLayerEntity.id,
            name = currentLayerEntity.name,
            date = currentLayerEntity.date,
            status = currentLayerEntity.status
    )

    override fun upstream(nextLayerEntity: TaskViewModelEntity) = TaskViewEntity(
            id = nextLayerEntity.id,
            name = nextLayerEntity.name,
            date = nextLayerEntity.date,
            status = nextLayerEntity.status
    )
}