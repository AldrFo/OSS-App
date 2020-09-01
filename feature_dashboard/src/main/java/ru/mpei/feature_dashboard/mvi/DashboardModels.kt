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
    }

    sealed class Dashboard : DashboardEvent() {
        data class DashboardListLoaded(val listOfItems: List<NewsItem>) : Dashboard()
        data class DashboardListLoadError(val throwable: Throwable) : Dashboard()
    }
}

sealed class DashboardEffect {
    data class ShowError(val throwable: Throwable) : DashboardEffect()
}

sealed class DashboardAction {
    object LoadDashboardList : DashboardAction()
}