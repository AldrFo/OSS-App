package ru.mpei.feature_profile.mvi

import kekmech.ru.common_mvi.Feature
import ru.mpei.domain_profile.dto.ParamsItem
import ru.mpei.domain_profile.dto.ProfileItem

typealias ProfileFeature = Feature<ProfileState, ProfileEvent, ProfileEffect>

data class ProfileState(
    val isLoading: Boolean = false,
    val profileData: ProfileItem = ProfileItem(),
    val params: ParamsItem = ParamsItem()
)

sealed class ProfileEvent{

    sealed class Wish: ProfileEvent(){
        object System{
            data class InitProfile(val id: String, val pass: String): Wish()
            object InitLogin: Wish()
        }
        data class LogIn(val email: String, val password: String): Wish()
        data class RefreshProfileData(val id: String, val pass: String): Wish()
    }

    sealed class News : ProfileEvent() {
        data class ProfileDataLoaded(val data: ProfileItem): News()
        data class ProfileDataLoadError(val throwable: Throwable): News()

        data class LogInSuccess(val params: ParamsItem): News()
        data class LogInFailed(val throwable: Throwable): News()
    }

}

sealed class ProfileEffect{
    data class ShowError(val throwable: Throwable): ProfileEffect()

    data class LogIn(val params: ParamsItem): ProfileEffect()
}

sealed class ProfileAction {
    data class LogIn(val email: String, val password: String): ProfileAction()
    data class LoadProfileData(val id: String, val pass: String): ProfileAction()
}