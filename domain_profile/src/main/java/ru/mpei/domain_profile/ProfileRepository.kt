package ru.mpei.domain_profile

import io.reactivex.Single
import ru.mpei.domain_profile.dto.ProfileItem

class ProfileRepository (
        private val profileApi: ProfileApi
){
    fun observeProfile(profileData: ProfileItem): Single<ProfileItem> = profileApi.get(profileData.id.toString(), profileData.hashPass)
}