package ru.mpei.feature_news.mvi

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result
import ru.mpei.feature_news.mvi.NewsEvent.News
import ru.mpei.feature_news.mvi.NewsEvent.Wish

typealias NewsResult = Result<NewsState, NewsEffect, NewsAction>

class NewsReducer : BaseReducer<NewsState, NewsEvent, NewsEffect, NewsAction> {

    override fun reduce(event: NewsEvent, state: NewsState): NewsResult = when (event) {
        is Wish -> processWish(event, state)
        is News -> processNews(event, state)
    }

    private fun processNews(event: News, state: NewsState): Result<NewsState, NewsEffect, NewsAction> = when (event) {
        is News.NewsListLoaded -> Result(
            state = state.copy(
                isLoading = false,
                listOfNews = event.listOfNews
            )
        )
        is News.NewsListLoadError -> Result(
            state = state.copy(isLoading = false)
        )
    }

    private fun processWish(event: Wish, state: NewsState): Result<NewsState, NewsEffect, NewsAction> = when (event) {
        is Wish.System.Init -> Result(
            state = state.copy(isLoading = true),
            action = NewsAction.LoadNewsList
        )
    }
}