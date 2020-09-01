package ru.mpei.feature_dashboard.mvi

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor
import ru.mpei.domain_news.NewsRepository

class DashboardActor(
    private val newsRepository: NewsRepository
) : Actor<DashboardAction, DashboardEvent> {

    override fun execute(action: DashboardAction): Observable<DashboardEvent> = when (action) {
        is DashboardAction.LoadDashboardList -> newsRepository.observeNews()
             .mapEvents(DashboardEvent.Dashboard::DashboardListLoaded, DashboardEvent.Dashboard::DashboardListLoadError)
    }
}