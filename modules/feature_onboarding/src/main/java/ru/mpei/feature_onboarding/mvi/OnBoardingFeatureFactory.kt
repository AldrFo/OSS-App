package ru.mpei.feature_onboarding.mvi

import kekmech.ru.common_mvi.BaseFeature

class OnBoardingFeatureFactory {

    fun create(): OnBoardingFeature = BaseFeature(
            initialState = OnBoardingState(),
            reducer = OnBoardingReducer(),
            actor = OnBoardingActor()
    ).start()
}