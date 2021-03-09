package ru.mpei.feature_onboarding.mvi

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result

typealias OnBoardingResult = Result<OnBoardingState, OnBoardingEffect, OnBoardingAction>

class OnBoardingReducer : BaseReducer<OnBoardingState, OnBoardingEvent, OnBoardingEffect, OnBoardingAction> {

    override fun reduce(event: OnBoardingEvent, state: OnBoardingState): OnBoardingResult {
        TODO("Not yet implemented")
    }
}