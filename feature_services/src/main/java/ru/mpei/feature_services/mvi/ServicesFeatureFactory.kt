package ru.mpei.feature_services.mvi

import kekmech.ru.common_mvi.BaseFeature

class ServicesFeatureFactory(
    actor: ServicesActor
) {
    fun create(): ServicesFeature = BaseFeature(
            initialState = ServicesState(),
            reducer = ServicesReducer(),
            actor = ServicesActor()
    ).start()
}