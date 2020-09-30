package ru.mpei.feature_profile.mvi

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor
import ru.mpei.domain_profile.ProfileRepository

class ProfileActor(
        private val profileRepository: ProfileRepository
) : Actor<ProfileAction, ProfileEvent>{
    override fun execute(action: ProfileAction): Observable<ProfileEvent> {
        TODO("Not yet implemented")
    }

}