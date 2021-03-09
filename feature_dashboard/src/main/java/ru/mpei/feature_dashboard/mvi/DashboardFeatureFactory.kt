package ru.mpei.feature_dashboard.mvi

import kekmech.ru.common_mvi.BaseFeature

class DashboardFeatureFactory(
    private val actor: DashboardActor
) {

    fun create(): DashboardFeature = BaseFeature(
        initialState = DashboardState(),
        reducer = DashboardReducer(),
        actor = actor
    ).start()
}