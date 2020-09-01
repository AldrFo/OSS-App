package ru.mpe.feature_tasks.mvi

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor

class TasksActor : Actor<TasksAction, TasksEvent> {
    override fun execute(action: TasksAction): Observable<TasksEvent> {
        TODO("Not yet implemented")
    }
}