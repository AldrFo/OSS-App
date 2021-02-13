package ru.mpei.domain_profile

import android.provider.ContactsContract
import io.reactivex.Single
import ru.mpei.domain_profile.dto.ParamsItem
import ru.mpei.domain_profile.dto.ProfileItem

class ProfileRepository (
        private val profileApi: ProfileApi
){
    /*fun observeProfile(id: String, pass: String): Single<ProfileItem> = profileApi.get(id, pass)
    fun login(email:String, password: String): Single<ParamsItem> = profileApi.login(email, password)*/

    fun authorize(id: String, pass: String): Single<ProfileItem> = profileApi.authorize(id, pass)

    fun authenticate(email: String, pass: String): Single<ParamsItem> = profileApi.authenticate(email, pass)
}