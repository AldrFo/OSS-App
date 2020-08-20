package ru.mpei.feature_news.mvi

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor

class NewsActor : Actor<NewsAction, NewsEvent> {

    override fun execute(action: NewsAction): Observable<NewsEvent> {
        TODO("Not yet implemented")
    }
}