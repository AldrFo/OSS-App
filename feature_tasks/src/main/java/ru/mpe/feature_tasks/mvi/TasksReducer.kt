package ru.mpe.feature_tasks.mvi

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result

typealias TasksResult = Result<TasksState, TasksEffect, TasksAction>

class TasksReducer : BaseReducer<TasksState, TasksEvent, TasksEffect, TasksAction> {
    override fun reduce(event: TasksEvent, state: TasksState):TasksResult {
        TODO("Not yet implemented")
    }
}