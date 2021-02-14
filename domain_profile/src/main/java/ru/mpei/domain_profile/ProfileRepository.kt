package ru.mpei.domain_profile

import android.provider.ContactsContract
import io.reactivex.Single
import ru.mpei.domain_profile.dto.ParamsItem
import ru.mpei.domain_profile.dto.ProfileItem

class ProfileRepository (
        private val profileApi: ProfileApi
){

    fun authorize(id: String, pass: String): Single<ProfileItem> = profileApi.authorize(id, pass)

    fun authenticate(email: String, pass: String): Single<ParamsItem> = profileApi.authenticate(email, pass)
}