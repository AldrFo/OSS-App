package ru.mpei.feature_profile.mvi

import kekmech.ru.common_mvi.Feature
import ru.mpei.domain_profile.dto.ParamsItem
import ru.mpei.domain_profile.dto.ProfileItem

typealias ProfileFeature = Feature<ProfileState, ProfileEvent, ProfileEffect>

data class ProfileState(
    val isLoading: Boolean = false,
    val profileData: ProfileItem = ProfileItem(),
    val isAuthorized: Boolean = false,
    val paramsItem: ParamsItem = ParamsItem()
)

sealed class ProfileEvent{

    sealed class Wish: ProfileEvent(){
        object System{
            object InitLogin: Wish()
        }

        data class Authorization(val id: String, val pass: String): Wish()

        object Authentication: Wish()

        data class ValidateFields(val email:String, val pass: String): Wish()
        object ValidationFailed: Wish()

        object Exit: Wish()

    }

    sealed class News : ProfileEvent() {

        data class Authorized(val profile: ProfileItem): News()
        data class AuthorizationFailed(val throwable: Throwable): News()

        data class Authenticated(val paramsItem: ParamsItem): News()
        data class AuthenticationFailed(val throwable: Throwable): News()
    }

}

sealed class ProfileEffect{
    data class AuthorizationError(val throwable: Throwable): ProfileEffect()
    data class AuthenticationError(val throwable: Throwable): ProfileEffect()
    data class SaveParams(val paramsItem: ParamsItem): ProfileEffect()
    data class Validate(val email: String, val pass: String): ProfileEffect()
    object ClearParams: ProfileEffect()
}

sealed class ProfileAction {
    data class Authorize(val id: String, val pass: String): ProfileAction()
    data class Authenticate(val email: String, val pass: String): ProfileAction()
}