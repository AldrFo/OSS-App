package ru.mpei.feature_onboarding.mvi

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor

class OnBoardingActor : Actor<OnBoardingAction, OnBoardingEvent> {

    override fun execute(action: OnBoardingAction): Observable<OnBoardingEvent> {
        TODO("Not yet implemented")
    }
}