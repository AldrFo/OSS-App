package ru.mpei.feature_profile

import android.os.Bundle
import android.view.View
import android.widget.TextView
import kekmech.ru.common_mvi.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.ext.android.inject
import ru.mpei.domain_profile.dto.ProfileItem
import ru.mpei.feature_profile.mvi.*
import ru.mpei.feature_profile.mvi.ProfileEvent.Wish

class  ProfileFragment: BaseFragment<ProfileEvent, ProfileEffect, ProfileState, ProfileFeature>(){
    override val initEvent: ProfileEvent get() = Wish.System.Init

    private val profileFeatureFactory: ProfileFeatureFactory by inject()

    override fun createFeature(): ProfileFeature = profileFeatureFactory.create()

    override var layoutId: Int = R.layout.fragment_profile

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        //
    }

    override fun render(state: ProfileState) {
        updateInfo(state)
    }

    override fun handleEffect(effect: ProfileEffect) = when(effect) {
        is ProfileEffect.ShowError -> Unit
    }

    private fun updateInfo(state: ProfileState) {
        profileName.text = "${state.profileData.name} ${state.profileData.surname}"
        profileCoins.text = "Ваш баланс: ${state.profileData.capital} р."
    }
}