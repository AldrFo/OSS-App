package ru.mpei.feature_tasks.mvi

/**
 * Андрей Турлюк
 * А-08-17
 */

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor
import ru.mpei.domain_tasks.AVAILABLE
import ru.mpei.domain_tasks.TasksRepository

// Класс исполнителя запросов на сервер
class TasksActor(private val tasksRepository: TasksRepository) : Actor<TasksAction, TasksEvent> {

    override fun execute(action: TasksAction) : Observable<TasksEvent> =
        when (action) {
            is TasksAction.LoadTasksList ->
                tasksRepository.observeTasks(action.id, AVAILABLE)
                    .mapEvents(TasksEvent.News::TasksLoaded, TasksEvent.News::TasksLoadError)

            is TasksAction.TakeTask ->
                tasksRepository.take(action.task)
                    .mapEvents(TasksEvent.News::TakeSuccess, TasksEvent.News::TakeError)
        }

}