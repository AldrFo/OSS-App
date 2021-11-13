package ru.mpei.feature_services.mvi

/**
 * Андрей Турлюк
 * А-08-17
 */

import kekmech.ru.common_mvi.BaseFeature

// Создатель фичи
class ServicesFeatureFactory(
    actor: ServicesActor
) {
    fun create(): ServicesFeature = BaseFeature(
            initialState = ServicesState(),
            reducer = ServicesReducer(),
            actor = ServicesActor()
    ).start()
}