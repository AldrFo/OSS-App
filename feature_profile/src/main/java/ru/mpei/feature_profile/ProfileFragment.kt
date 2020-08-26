package ru.mpei.feature_profile

import android.os.Bundle
import android.view.View
import kekmech.ru.common_mvi.ui.BaseFragment
import ru.mpei.feature_profile.mvi.*
import ru.mpei.feature_profile.mvi.ProfileEvent.Wish

class  ProfileFragment: BaseFragment<ProfileEvent, ProfileEffect, ProfileState, ProfileFeature>(){
    override val initEvent: ProfileEvent get() = Wish.System.Init

    override fun createFeature(): ProfileFeature {
        TODO("Not yet implemented")
    }

    override var layoutId: Int = R.layout.fragment_profile

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        //
    }

    override fun render(state: ProfileState) {
        //
    }

    override fun handleEffect(effect: ProfileEffect) = when(effect) {
        is ProfileEffect.ShowError -> Unit
    }
}