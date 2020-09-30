package ru.mpei.feature_profile.mvi

import kekmech.ru.common_mvi.BaseFeature

class ProfileFeatureFactory(
        private val actor: ProfileActor
) {
    fun create(): ProfileFeature = BaseFeature(
            initialState = ProfileState(),
            reducer = ProfileReducer(),
            actor = actor
    ).start()
}