package ru.mpei.feature_profile.mvi

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor
import ru.mpei.domain_profile.ProfileRepository
import ru.mpei.domain_profile.dto.ProfileItem

class ProfileActor(
        private val profileRepository: ProfileRepository
) : Actor<ProfileAction, ProfileEvent>{
    override fun execute(action: ProfileAction): Observable<ProfileEvent> = when (action){
        is ProfileAction.LoadProfileData -> profileRepository.observeProfile(ProfileItem())
                .mapEvents(ProfileEvent.News::ProfileDataLoaded, ProfileEvent.News::ProfileDataLoadError)
    }

}