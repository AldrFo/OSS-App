package ru.mpei.feature_dashboard.mvi

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result
import ru.mpei.feature_dashboard.mvi.DashboardEvent.News
import ru.mpei.feature_dashboard.mvi.DashboardEvent.Wish

typealias RefactorResult = Result<DashboardState, DashboardEffect, DashboardAction>

class DashboardReducer : BaseReducer<DashboardState, DashboardEvent, DashboardEffect, DashboardAction> {

    override fun reduce(event: DashboardEvent, state: DashboardState): RefactorResult = when (event) {
        is Wish -> processWish(event, state)
        is News -> processItems(event, state)
    }

    private fun processItems(event: News, state: DashboardState): Result<DashboardState, DashboardEffect, DashboardAction> = when (event) {
        is News.NewsListLoaded -> Result(
            state = state.copy(
                isLoading = false,
                listOfItems = event.listOfItems
            )
        )
        is News.NewsListLoadError -> Result(
            state = state.copy(isLoading = false)
        )
    }

    private fun processWish(event: Wish, state: DashboardState): Result<DashboardState, DashboardEffect, DashboardAction> = when (event) {
        is Wish.System.Init -> Result(
            state = state.copy(isLoading = true),
            action = DashboardAction.LoadDashboardList
        )
        is Wish.OnSwipeRefresh -> Result(
            state = state.copy()
        )
    }
}