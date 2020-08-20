package ru.mpei.feature_news.mvi

import kekmech.ru.common_mvi.BaseFeature

class NewsFeatureFactory {

    fun create(): NewsFeature = BaseFeature(
        initialState = NewsState(),
        reducer = NewsReducer(),
        actor = NewsActor()
    ).start()
}