package ru.mpei.feature_dashboard.mvi

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor
import ru.mpei.domain_news.NewsRepository

class DashboardActor(
    private val newsRepository: NewsRepository
) : Actor<DashboardAction, DashboardEvent> {

    override fun execute(action: DashboardAction): Observable<DashboardEvent> = when (action) {
        is DashboardAction.LoadNewsList -> newsRepository.observeNews()
                .mapEvents(DashboardEvent.News::NewsListLoaded, DashboardEvent.News::NewsListLoadError)
        is DashboardAction.LoadEventsList -> newsRepository.observeEvents()
                .mapEvents(DashboardEvent.News::EventsListLoaded, DashboardEvent.News::EventsListLoadError)
    }
}