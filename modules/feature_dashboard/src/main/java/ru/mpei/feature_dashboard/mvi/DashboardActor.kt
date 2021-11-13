package ru.mpei.feature_dashboard.mvi

/**
 * Андрей Турлюк
 * А-08-17
 */
import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor
import ru.mpei.domain_news.NewsRepository

// Исполнитель событий для работы с сервером в рамках архитектуры MVI
class DashboardActor(
    private val newsRepository: NewsRepository
) : Actor<DashboardAction, DashboardEvent> {

    override fun execute(action: DashboardAction): Observable<DashboardEvent> = when (action) {
        // Обработка запроса загрузки списка новостей
        is DashboardAction.LoadNewsList -> newsRepository.observeNews()
                .mapEvents(DashboardEvent.News::NewsListLoaded, DashboardEvent.News::NewsListLoadError)
        // Обработка запроса загрузкисписка событий
        is DashboardAction.LoadEventsList -> newsRepository.observeEvents()
                .mapEvents(DashboardEvent.News::EventsListLoaded, DashboardEvent.News::EventsListLoadError)
    }
}