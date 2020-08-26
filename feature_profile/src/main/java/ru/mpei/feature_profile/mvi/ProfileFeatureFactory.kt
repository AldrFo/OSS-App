package ru.mpei.feature_profile.mvi

import kekmech.ru.common_mvi.BaseFeature

class ProfileFeatureFactory {
    fun create(): ProfileFeature = BaseFeature(
            initialState = ProfileState(),
            reducer = ProfileReducer(),
            actor = ProfileActor()
    ).start()
}