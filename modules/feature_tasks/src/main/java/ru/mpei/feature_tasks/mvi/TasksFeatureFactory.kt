package ru.mpei.feature_tasks.mvi

/**
 * Андрей Турлюк
 * А-08-17
 */
import kekmech.ru.common_mvi.BaseFeature

// Создатель фичи
class TasksFeatureFactory(
        private val actor: TasksActor
) {
    fun create(): TasksFeature = BaseFeature(
            initialState = TasksState(),
            reducer = TasksReducer(),
            actor = actor
    ).start()

    fun createTask(): TasksFeature = BaseFeature(
        initialState = TasksState(),
        reducer = TasksReducer(),
        actor = actor
    ).start()
}