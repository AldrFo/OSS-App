package ru.mpei.feature_tasks.mvi

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result
import ru.mpei.feature_tasks.mvi.TasksEvent.*

typealias TasksResult = Result<TasksState, TasksEffect, TasksAction>

class TasksReducer : BaseReducer<TasksState, TasksEvent, TasksEffect, TasksAction> {
    override fun reduce(event: TasksEvent, state: TasksState): TasksResult = when(event) {
        is Wish -> processWish(event, state)
        is News -> processItems(event, state)
    }

    private fun processWish(event: Wish, state: TasksState): TasksResult = when (event) {
        is Wish.System.Init -> Result(
            state = state.copy(isLoading = true),
            action = TasksAction.LoadTasksList(event.id)
        )
        is Wish.System.InitTask -> Result(
            state = state.copy()
        )
        is Wish.TakeTask -> Result(
            state = state.copy(
                isLoading = true
            ),
            action = TasksAction.TakeTask(event.task)
        )
    }

    private fun processItems(event: News, state: TasksState): TasksResult = when (event) {
        is News.TasksLoaded -> Result(
                state.copy(
                        isLoading = false,
                        ListOfTasks = event.ListOfTasks
                )
        )
        is News.TasksLoadError -> Result(
                state = state.copy(isLoading = false)
        )
        is News.TakeSuccess -> Result(
            state.copy(
                isLoading = false
            ),
            effect = TasksEffect.TaskTaken
        )
        is News.TakeError -> Result(
            state.copy(
                isLoading = false
            ),
            effect = TasksEffect.ShowError(event.throwable)
        )
    }
}