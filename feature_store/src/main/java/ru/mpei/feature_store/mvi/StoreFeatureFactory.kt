package ru.mpei.feature_store.mvi

import kekmech.ru.common_mvi.BaseFeature

class StoreFeatureFactory {
    fun create(): StoreFeature = BaseFeature(
        initialState = StoreState(),
        reducer = StoreReducer(),
        actor = StoreActor()
    ).start()
}