package ru.mpei.feature_news.mvi

import kekmech.ru.common_mvi.Feature
import ru.mpei.domain_news.dto.NewsItem

typealias NewsFeature = Feature<NewsState, NewsEvent, NewsEffect>

data class NewsState(
    val isLoading: Boolean = false,
    val listOfNews: List<NewsItem> = emptyList()
)

sealed class NewsEvent {

    sealed class Wish : NewsEvent() {
        object System {
            object Init : Wish()
        }
    }

    sealed class News : NewsEvent() {
        data class NewsListLoaded(val listOfNews: List<NewsItem>) : News()
        data class NewsListLoadError(val throwable: Throwable) : News()
    }
}

sealed class NewsEffect {
    data class ShowError(val throwable: Throwable) : NewsEffect()
}

sealed class NewsAction {
    object LoadNewsList : NewsAction()
}