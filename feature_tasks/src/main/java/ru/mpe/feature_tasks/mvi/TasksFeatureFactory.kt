package ru.mpe.feature_tasks.mvi

import kekmech.ru.common_mvi.BaseFeature

class TasksFeatureFactory {
    fun create(): TasksFeature = BaseFeature(
            initialState = TasksState(),
            reducer = TasksReducer(),
            actor = TasksActor()
    ).start()
}