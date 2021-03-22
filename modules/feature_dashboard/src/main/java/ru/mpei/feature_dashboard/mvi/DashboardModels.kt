package ru.mpei.feature_dashboard.mvi

import kekmech.ru.common_mvi.Feature
import ru.mpei.domain_news.dto.NewsItem

typealias DashboardFeature = Feature<DashboardState, DashboardEvent, DashboardEffect>

data class DashboardState(
    val isLoading: Boolean = false,
    val newsList: List<NewsItem> = emptyList(),
    val eventsList: List<NewsItem> = emptyList(),
    val selectedPage: Int = 0
)

sealed class DashboardEvent {

    sealed class Wish : DashboardEvent() {
        object System {
            object Init : Wish()
        }

        object GetEvents : Wish()
        object GetNews : Wish()
        data class OnPageChange(val position: Int): Wish()
    }

    sealed class News : DashboardEvent() {
        data class NewsListLoaded(val listOfItems: List<NewsItem>) : News()
        data class NewsListLoadError(val throwable: Throwable) : News()

        data class EventsListLoaded(val listOfItems: List<NewsItem>) : News()
        data class EventsListLoadError(val throwable: Throwable) : News()
    }
}

sealed class DashboardEffect {
    object NewsListLoaded: DashboardEffect()
    object EventsListLoaded: DashboardEffect()
    data class ShowError(val throwable: Throwable) : DashboardEffect()
}

sealed class DashboardAction {
    object LoadNewsList : DashboardAction()
    object LoadEventsList : DashboardAction()
}