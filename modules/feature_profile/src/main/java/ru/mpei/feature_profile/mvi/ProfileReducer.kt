package ru.mpei.feature_profile.mvi

/**
 * Андрей Турлюк
 * А-08-17
 */

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result
import ru.mpei.domain_profile.dto.ParamsItem
import ru.mpei.feature_profile.mvi.ProfileEvent.News
import ru.mpei.feature_profile.mvi.ProfileEvent.Wish

typealias ProfileResult = Result<ProfileState, ProfileEffect, ProfileAction>

// Обработчик намерений и события
class ProfileReducer : BaseReducer<ProfileState, ProfileEvent, ProfileEffect, ProfileAction>{
    override fun reduce(event: ProfileEvent, state: ProfileState): ProfileResult  = when (event) {
        is Wish -> processWish(event, state)
        is News -> processItems(event, state)
    }

    private fun processItems(event: News, state: ProfileState): ProfileResult = when (event) {

        is News.TaskRefused -> Result(
            state = state.copy(
                isLoading = false
            ),
            effect = ProfileEffect.RefuseSuccess
        )

        is News.TaskRefuseError -> Result(
            state = state.copy(
                isLoading = false
            ),
            effect = ProfileEffect.RefuseError(event.throwable)
        )

        is News.TaskConfirmed -> Result(
            state = state.copy(
                isLoading = false
            ),
            effect = ProfileEffect.ConfirmSuccess
        )

        is News.TaskConfirmError -> Result(
            state = state.copy(
                isLoading = false
            ),
            effect = ProfileEffect.ConfirmError(event.throwable)
        )

        is News.ReportSent -> Result(
            state = state.copy(
                isLoading = false
            ),
            effect = ProfileEffect.ReportSendSuccess
        )

        is News.ReportSendError -> Result(
            state = state.copy(
                isLoading = false
            ),
            effect = ProfileEffect.ReportSendError(event.throwable)
        )

        is News.Authorized -> Result(
            state = state.copy(
                isLoading = false,
                profileData = event.profile,
                isAuthorized = true
            )
        )

        is News.AuthorizationFailed -> Result(
            state = ProfileState(),
            effect = ProfileEffect.AuthorizationError(event.throwable)
        )

        is News.Authenticated -> Result(
            state = state.copy(
                isLoading = true,
                paramsItem = event.paramsItem
            ),
            action = ProfileAction.Authorize(event.paramsItem.id, event.paramsItem.pass),
            effect = ProfileEffect.SaveParams(event.paramsItem)
        )

        is News.AuthenticationFailed -> Result(
            state = ProfileState(),
            effect = ProfileEffect.AuthenticationError(event.throwable)
        )

        is News.TasksLoaded -> Result(
            state = state.copy(
                isLoading = false,
                tasksList = event.tasksList
            ),
            effect = ProfileEffect.TasksLoaded
        )

        is News.TasksLoadFailed -> Result(
            state = state.copy(
                isLoading = false
            ),
            effect = ProfileEffect.TasksLoadError(event.throwable)
        )

        is News.AllProductsLoaded -> Result(
            state = state.copy(
                isLoading = false,
                shopAllProductsList = event.allProducts
            ),
            effect = ProfileEffect.AllProductsLoaded
        )

        is News.AllProductsLoadError -> Result(
            state = state.copy(
                isLoading = false
            ),
            effect = ProfileEffect.ShowError(event.throwable)
        )

        is News.PopularProductsLoaded -> Result(
            state = state.copy(
                isLoading = false,
                shopPopularProductsList = event.popularProducts
            ),
            effect = ProfileEffect.PopularProductsLoaded
        )

        is News.PopularProductsLoadError -> Result(
            state = state.copy(
                isLoading = false
            ),
            effect = ProfileEffect.ShowError(event.throwable)
        )

    }

    private fun processWish(event: Wish, state: ProfileState): ProfileResult = when (event) {

        is Wish.System.InitLogin -> Result(
            state = ProfileState()
        )

        is Wish.System.InitReport -> Result(
            state = state.copy()
        )

        is Wish.System.InitTask -> Result(
            state = state.copy()
        )

        is Wish.System.InitShop -> Result(
            state = state.copy(
                isLoading = true
            ),
            effects = emptyList(),
            actions = listOf(
                ProfileAction.LoadPopularProducts,
                ProfileAction.LoadAllProducts
            )
        )

        is Wish.AddPhoto -> Result(
            state = state.copy(),
            effect = ProfileEffect.AddPhoto
        )

        is Wish.OpenShop -> Result(
            state = state.copy(),
            effect = ProfileEffect.OpenShop
        )

        is Wish.ConfirmTask -> Result(
            state = state.copy(
                isLoading = true
            ),
            action = ProfileAction.ConfirmTask(body = event.body)
        )

        is Wish.RefuseTask -> Result(
            state = state.copy(
                isLoading = true
            ),
            action = ProfileAction.RefuseTask(event.body)
        )

        is Wish.SendReport -> Result(
            state = state.copy(
                isLoading = true
            ),
            action = ProfileAction.SendReport(body = event.body, imageBody = event.imageBody)
        )

        is Wish.Authorization -> Result(
            state = state.copy(
                isLoading = true,
                paramsItem = ParamsItem(id = event.id, pass = event.pass)
            ),
            action = ProfileAction.Authorize(event.id, event.pass)
        )

        is Wish.ValidateFields -> Result(
            state = state.copy(
                paramsItem = ParamsItem(email = event.email, pass = event.pass)
            ),
            effect = ProfileEffect.Validate(email = event.email, pass = event.pass)
        )

        is Wish.ValidationFailed -> Result(
            state = ProfileState()
        )

        is Wish.Authentication -> Result(
            state = state.copy(
                isLoading = true
            ),
            action = ProfileAction.Authenticate(state.paramsItem.email, state.paramsItem.pass)
        )

        is Wish.Exit -> Result(
            state = ProfileState(),
            effect = ProfileEffect.ClearParams
        )

        is Wish.LoadTasks -> Result(
            state = state.copy(
                isLoading = true
            ),
            action = ProfileAction.LoadTasks(event.type, state.profileData.id.toString())
        )

        is Wish.OnShopPageChange -> Result(
            state = state.copy(
                selectedShopPage = event.position
            )
        )

        is Wish.LoadAllProducts -> Result(
            state = state.copy(
                isLoading = true
            ),
            action = ProfileAction.LoadAllProducts
        )

        is Wish.LoadPopularProducts -> Result(
            state = state.copy(
                isLoading = true
            ),
            action = ProfileAction.LoadPopularProducts
        )
    }
}