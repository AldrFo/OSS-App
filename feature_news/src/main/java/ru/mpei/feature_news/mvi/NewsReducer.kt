package ru.mpei.feature_news.mvi

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result

typealias NewsResult = Result<NewsState, NewsEffect, NewsAction>

class NewsReducer : BaseReducer<NewsState, NewsEvent, NewsEffect, NewsAction> {

    override fun reduce(event: NewsEvent, state: NewsState): NewsResult {
        TODO("Not yet implemented")
    }
}