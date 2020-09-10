package ru.mpe.feature_tasks.mvi

import kekmech.ru.common_mvi.Feature
import ru.mpei.domain_tasks.dto.TasksItem

typealias TasksFeature = Feature<TasksState, TasksEvent, TasksEffect>

data class TasksState(
        val isLoading: Boolean = false,
        val ListOfTasks: List<TasksItem> = emptyList()
)

sealed class TasksEvent{

    sealed class Wish: TasksEvent(){
        object System{
            object Init: Wish()
        }
    }

    sealed class News : TasksEvent() {
        data class TasksLoaded(val ListOfTasks: List<TasksItem>): News()
        data class TasksLoadError(val throwable: Throwable): News()
    }
}

sealed class TasksEffect{
    data class ShowError(val throwable: Throwable): TasksEffect()
}

sealed class TasksAction{
    object LoadTasksList: TasksAction()
}