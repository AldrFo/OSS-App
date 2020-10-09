package ru.mpei.domain_profile

import io.reactivex.Single
import kekmech.ru.common_annotations.EndpointUrl
import retrofit2.http.GET
import retrofit2.http.Query
import ru.mpei.domain_profile.dto.ParamsItem
import ru.mpei.domain_profile.dto.ProfileItem

@EndpointUrl("http://cy37212.tmweb.ru/")
interface ProfileApi {

    @GET("Android/lk.php")
    fun get(@Query("id") id: String, @Query("pass") password: String): Single<ProfileItem>

    @GET("Android/auth.php")
    fun login(@Query("email") email: String, @Query("password") password: String): Single<ParamsItem>

}