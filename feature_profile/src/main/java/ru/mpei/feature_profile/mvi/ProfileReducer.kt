package ru.mpei.feature_profile.mvi

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result
import ru.mpei.feature_profile.mvi.ProfileEvent.News
import ru.mpei.feature_profile.mvi.ProfileEvent.Wish

typealias ProfileResult = Result<ProfileState, ProfileEffect, ProfileAction>

class ProfileReducer : BaseReducer<ProfileState, ProfileEvent, ProfileEffect, ProfileAction>{
    override fun reduce(event: ProfileEvent, state: ProfileState): ProfileResult  = when (event) {
        is Wish -> processWish(event, state)
        is News -> processItems(event, state)
    }

    private fun processItems(event: News, state: ProfileState): ProfileResult = when (event) {
        is News.ProfileDataLoaded -> Result(
            state = state.copy(
                isLoading = false,
                profileData = event.data
            )
        )
        is News.ProfileDataLoadError -> Result(
            state = state.copy(isLoading = false)
        )
    }

    private fun processWish(event: Wish, state: ProfileState): ProfileResult = when (event) {
        is Wish.System.Init -> Result(
            state = state.copy(isLoading = true),
            action = ProfileAction.LoadProfileData
        )
        is Wish.RefreshProfileData -> Result(
            state = state.copy(isLoading = true),
            action = ProfileAction.LoadProfileData
        )
    }
}