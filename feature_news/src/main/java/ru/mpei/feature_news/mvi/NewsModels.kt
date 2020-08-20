package ru.mpei.feature_news.mvi

import kekmech.ru.common_mvi.Feature

typealias NewsFeature = Feature<NewsState, NewsEvent, NewsEffect>

data class NewsState(
    val isLoading: Boolean = false,
    val listOfNews: List<Any> = emptyList()
)

sealed class NewsEvent {

    sealed class Wish : NewsEvent() {
        object System {
            object Init : Wish()
        }
    }

    sealed class News : NewsEvent() {

    }
}

sealed class NewsEffect {
    data class ShowError(val throwable: Throwable) : NewsEffect()
}

sealed class NewsAction {

}