package ru.mpei.feature_tasks.mvi

import kekmech.ru.common_mvi.Feature
import okhttp3.ResponseBody
import ru.mpei.domain_tasks.dto.TakeTaskItem
import ru.mpei.domain_tasks.dto.TasksItem

typealias TasksFeature = Feature<TasksState, TasksEvent, TasksEffect>

data class TasksState(
        val isLoading: Boolean = false,
        val ListOfTasks: List<TasksItem> = emptyList(),
        val isTaskTaken: Boolean = false
)

sealed class TasksEvent{

    sealed class Wish: TasksEvent(){
        object System{
            data class Init(val id: String): Wish()
            object InitTask: Wish()
        }
        data class TakeTask(val task: TakeTaskItem): Wish()
    }

    sealed class News : TasksEvent() {
        data class TasksLoaded(val ListOfTasks: List<TasksItem>): News()
        data class TasksLoadError(val throwable: Throwable): News()
        data class TakeSuccess(val obj: ResponseBody): News()
        data class TakeError(val throwable: Throwable): News()
    }
}

sealed class TasksEffect{
    data class ShowError(val throwable: Throwable): TasksEffect()
    object TaskTaken: TasksEffect()
    object TasksLoaded: TasksEffect()
}

sealed class TasksAction{
    data class LoadTasksList(val id: String): TasksAction()
    data class TakeTask(val task: TakeTaskItem): TasksAction()
}