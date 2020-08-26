package ru.mpei.feature_profile.mvi

import kekmech.ru.common_mvi.Feature

typealias ProfileFeature = Feature<ProfileState, ProfileEvent, ProfileEffect>

data class ProfileState(
        val isLoading: Boolean = false,
        val listOfNews: List<Any> = emptyList()
)

sealed class ProfileEvent{

    sealed class Wish: ProfileEvent(){
        object System{
            object Init: Wish()
        }
    }

    sealed class Profile : ProfileEvent() {

    }

}

sealed class ProfileEffect{
    data class ShowError(val throwable: Throwable): ProfileEffect()
}

sealed class ProfileAction {
    
}