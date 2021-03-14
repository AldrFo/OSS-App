package ru.mpei.feature_profile.mvi

import kekmech.ru.common_mvi.BaseFeature
import ru.mpei.domain_profile.dto.ProfileItem

class ProfileFeatureFactory(
        private val actor: ProfileActor
) {
    fun create(): ProfileFeature = BaseFeature(
            initialState = ProfileState(),
            reducer = ProfileReducer(),
            actor = actor
    ).start()

    fun createWithData(profileData: ProfileItem) : ProfileFeature = BaseFeature(
        initialState = ProfileState(
            profileData = profileData
        ),
        reducer = ProfileReducer(),
        actor = actor
    ).start()
}