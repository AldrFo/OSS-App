package ru.mpei.feature_profile.mvi

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result

typealias ProfileResult = Result<ProfileState, ProfileEffect, ProfileAction>

class ProfileReducer : BaseReducer<ProfileState, ProfileEvent, ProfileEffect, ProfileAction>{
    override fun reduce(event: ProfileEvent, state: ProfileState): Result<ProfileState, ProfileEffect, ProfileAction> {
        TODO("Not yet implemented")
    }
}