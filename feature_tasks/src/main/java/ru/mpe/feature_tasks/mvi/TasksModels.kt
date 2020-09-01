package ru.mpe.feature_tasks.mvi

import kekmech.ru.common_mvi.Feature

typealias TasksFeature = Feature<TasksState, TasksEvent, TasksEffect>

data class TasksState(
        val isLoading: Boolean = false,
        val ListOfNews: List<Any> = emptyList()
)

sealed class TasksEvent{

    sealed class Wish: TasksEvent(){
        object System{
            object Init: Wish()
        }
    }

    sealed class Store : TasksEvent() {

    }
}

sealed class TasksEffect{
    data class ShowError(val throwable: Throwable): TasksEffect()
}

sealed class TasksAction{

}