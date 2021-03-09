package ru.mpei.feature_profile.mvi

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor
import ru.mpei.domain_profile.ProfileRepository

class ProfileActor(
        private val profileRepository: ProfileRepository,
) : Actor<ProfileAction, ProfileEvent>{
    override fun execute(action: ProfileAction): Observable<ProfileEvent> = when (action){

        is ProfileAction.Authorize -> profileRepository.authorize(action.id, action.pass)
            .mapEvents(ProfileEvent.News::Authorized, ProfileEvent.News::AuthorizationFailed)

        is ProfileAction.Authenticate -> profileRepository.authenticate(action.email, action.pass)
            .mapEvents(ProfileEvent.News::Authenticated, ProfileEvent.News::AuthenticationFailed)

        is ProfileAction.LoadTasks -> profileRepository.loadTasks(action.type, action.id)
            .mapEvents(ProfileEvent.News::TasksLoaded, ProfileEvent.News::TasksLoadFailed)
    }
}