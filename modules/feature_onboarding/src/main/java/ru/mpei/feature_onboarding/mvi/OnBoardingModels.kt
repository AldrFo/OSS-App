package ru.mpei.feature_onboarding.mvi

import kekmech.ru.common_mvi.Feature

typealias OnBoardingFeature = Feature<OnBoardingState, OnBoardingEvent, OnBoardingEffect>

data class OnBoardingState(
        val isLoading: Boolean = false,
        val listOfNews: List<Any> = emptyList()
)

sealed class OnBoardingEvent {

    sealed class Wish : OnBoardingEvent() {
        object System {
            object Init : Wish()
        }
    }

    sealed class News : OnBoardingEvent() {

    }
}

sealed class OnBoardingEffect {
    data class ShowError(val throwable: Throwable) : OnBoardingEffect()
}

sealed class OnBoardingAction {

}