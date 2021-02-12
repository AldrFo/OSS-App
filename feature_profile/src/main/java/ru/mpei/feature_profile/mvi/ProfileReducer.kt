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
                profileData = event.data,
            )
        )
        is News.ProfileDataLoadError -> Result(
            state = state.copy(isLoading = false),
            effect = ProfileEffect.ShowError(event.throwable)
        )
        is News.LogInSuccess -> Result(
            state = state.copy(
                isLoading = false,
                params = event.params,
                isAuthorized = true
            ),
            effect = ProfileEffect.LogIn(event.params)
        )
        is News.LogInFailed -> Result(
            state = state.copy(
                isLoading = false
            ),
            effect = ProfileEffect.ShowError(event.throwable)
        )
    }

    private fun processWish(event: Wish, state: ProfileState): ProfileResult = when (event) {
        is Wish.System.InitProfile -> Result(
            state = state.copy(isLoading = true),
            action = ProfileAction.LoadProfileData(event.id, event.pass)
        )
        is Wish.System.InitLogin -> Result(
            state = state
        )
        is Wish.RefreshProfileData -> Result(
            state = state.copy(isLoading = true),
            action = ProfileAction.LoadProfileData(event.id, event.pass)
        )
        is Wish.LogIn -> Result(
            state = state.copy(isLoading = true),
            action = ProfileAction.LogIn(event.email, event.password)
        )
    }
}