package ru.mpei.feature_tasks.mvi

import kekmech.ru.common_mvi.BaseFeature

class TasksFeatureFactory(
        private val actor: TasksActor
) {
    fun create(): TasksFeature = BaseFeature(
            initialState = TasksState(),
            reducer = TasksReducer(),
            actor = actor
    ).start()
}