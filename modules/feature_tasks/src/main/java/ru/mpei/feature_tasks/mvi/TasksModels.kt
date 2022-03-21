package ru.mpei.feature_tasks.mvi

/**
 * Андрей Турлюк
 * А-08-17
 */
import kekmech.ru.common_mvi.Feature
import okhttp3.ResponseBody
import ru.mpei.domain_tasks.dto.TakeTaskItem
import ru.mpei.domain_tasks.dto.TasksItem

typealias TasksFeature = Feature<TasksState, TasksEvent, TasksEffect>

// Состояние фичи
data class TasksState(
    val isLoading: Boolean = false,
    val ListOfTasks: List<TasksItem> = emptyList(),
    val isTaskTaken: Boolean = false
)

sealed class TasksEvent{

    sealed class Wish: TasksEvent(){
        // Ннамерение инициализации фрагмента
        object System{
            data class Init(val id: String): Wish()
            object InitTask: Wish()
        }
        // Намерение взятия задания
        data class TakeTask(val task: TakeTaskItem): Wish()
    }

    sealed class News : TasksEvent() {
        // Событие успешной загрузки заданй
        data class TasksLoaded(val ListOfTasks: List<TasksItem>): News()
        // Событие ошибки прпи заагрузке списка заданий
        data class TasksLoadError(val throwable: Throwable): News()
        // Событие успешногоо взятия задания на выполнение
        data class TakeSuccess(val obj: ResponseBody): News()
        // Событие ошибки при взятии задааааания выполнение
        data class TakeError(val throwable: Throwable): News()
    }
}

sealed class TasksEffect{
    // Эффект отображения ошибки
    data class ShowError(val throwable: Throwable): TasksEffect()
    // Эффект взятия задания на выполнение
    object TaskTaken: TasksEffect()
    // Эффект загрузки списка заданий
    object TasksLoaded: TasksEffect()
}

sealed class TasksAction{
    // Действие загрузки списка заданий
    data class LoadTasksList(val id: String): TasksAction()
    // Действие взятия задания на выполнение
    data class TakeTask(val task: TakeTaskItem): TasksAction()
}