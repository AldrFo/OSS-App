package ru.mpei.feature_dashboard.mvi
/**
 * Андрей Турлюк
 * А-08-17
 */

import kekmech.ru.common_mvi.BaseFeature

// Создание элемента feature в рамках архитектуры MVI
class DashboardFeatureFactory(
    private val actor: DashboardActor
) {

    fun create(): DashboardFeature = BaseFeature(
        initialState = DashboardState(),
        reducer = DashboardReducer(),
        actor = actor
    ).start()
}