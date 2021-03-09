package ru.mpei.feature_onboarding

import android.os.Bundle
import android.view.View
import kekmech.ru.common_mvi.ui.BaseFragment
import ru.mpei.feature_onboarding.mvi.*
import ru.mpei.feature_onboarding.mvi.OnBoardingEvent.*

class OnBoardingFragment : BaseFragment<OnBoardingEvent, OnBoardingEffect, OnBoardingState, OnBoardingFeature>() {
    override val initEvent: OnBoardingEvent get() = Wish.System.Init

    override fun createFeature(): OnBoardingFeature = OnBoardingFeatureFactory().create()

    override var layoutId = R.layout.fragment_onboarding

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        //
    }

    override fun render(state: OnBoardingState) {
        //
    }

    override fun handleEffect(effect: OnBoardingEffect) = when(effect) {
        is OnBoardingEffect.ShowError -> Unit
    }
}