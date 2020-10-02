package ru.mpei.feature_profile.mvi

import kekmech.ru.common_mvi.Feature
import ru.mpei.domain_profile.dto.ProfileItem

typealias ProfileFeature = Feature<ProfileState, ProfileEvent, ProfileEffect>

data class ProfileState(
        val isLoading: Boolean = false,
        val profileData: ProfileItem = ProfileItem()
)

sealed class ProfileEvent{

    sealed class Wish: ProfileEvent(){
        object System{
            object Init: Wish()
        }

        object RefreshProfileData: Wish()
    }

    sealed class News : ProfileEvent() {
        data class ProfileDataLoaded(val data: ProfileItem): News()
        data class ProfileDataLoadError(val throwable: Throwable): News()
    }

}

sealed class ProfileEffect{
    data class ShowError(val throwable: Throwable): ProfileEffect()
}

sealed class ProfileAction {
    object LoadProfileData: ProfileAction()
}