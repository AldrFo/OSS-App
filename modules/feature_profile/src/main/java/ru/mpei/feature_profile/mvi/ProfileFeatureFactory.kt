package ru.mpei.feature_profile.mvi

/**
 * Андрей Турлюк
 * А-08-17
 */

import kekmech.ru.common_mvi.BaseFeature
import ru.mpei.domain_profile.dto.ProfileItem

//Создатель фичи
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