package ru.mpei.feature_profile.mvi

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor
import ru.mpei.domain_profile.ProfileRepository

class ProfileActor(
        private val profileRepository: ProfileRepository,
) : Actor<ProfileAction, ProfileEvent>{
    override fun execute(action: ProfileAction): Observable<ProfileEvent> = when (action){
        is ProfileAction.LoadProfileData -> profileRepository.observeProfile(action.id, action.pass)
                .mapEvents(ProfileEvent.News::ProfileDataLoaded, ProfileEvent.News::ProfileDataLoadError)
        is ProfileAction.LogIn -> profileRepository.login(action.email, action.password)
                .mapEvents(ProfileEvent.News::LogInSuccess, ProfileEvent.News::LogInFailed)
    }
}