package ru.mpei.feature_dashboard.mvi

import kekmech.ru.common_mvi.Feature
import ru.mpei.domain_news.dto.NewsItem

typealias DashboardFeature = Feature<DashboardState, DashboardEvent, DashboardEffect>

data class DashboardState(
    val isLoading: Boolean = false,
    val listOfItems: List<NewsItem> = emptyList()
)

sealed class DashboardEvent {

    sealed class Wish : DashboardEvent() {
        object System {
            object Init : Wish()
        }

        object OnSwipeRefresh : Wish()
        // data class OnSwipeRefresh(kek: Boolean) : Wish()
    }

    sealed class News : DashboardEvent() {
        data class NewsListLoaded(val listOfItems: List<NewsItem>) : News()
        data class NewsListLoadError(val throwable: Throwable) : News()
    }
}

sealed class DashboardEffect {
    data class ShowError(val throwable: Throwable) : DashboardEffect()
}

sealed class DashboardAction {
    object LoadDashboardList : DashboardAction()
}