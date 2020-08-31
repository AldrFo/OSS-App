package ru.mpei.feature_news.mvi

import kekmech.ru.common_mvi.BaseFeature

class NewsFeatureFactory(
    private val actor: NewsActor
) {

    fun create(): NewsFeature = BaseFeature(
        initialState = NewsState(),
        reducer = NewsReducer(),
        actor = actor
    ).start()
}