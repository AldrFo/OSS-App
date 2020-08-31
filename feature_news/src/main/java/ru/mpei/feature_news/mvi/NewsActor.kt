package ru.mpei.feature_news.mvi

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor
import ru.mpei.domain_news.NewsRepository

class NewsActor(
    private val newsRepository: NewsRepository
) : Actor<NewsAction, NewsEvent> {

    override fun execute(action: NewsAction): Observable<NewsEvent> = when (action) {
        is NewsAction.LoadNewsList -> newsRepository.observeNews()
             .mapEvents(NewsEvent.News::NewsListLoaded, NewsEvent.News::NewsListLoadError)
    }
}