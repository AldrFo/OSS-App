package ru.mpei.vmss.domain_profile

import io.reactivex.Single
import kekmech.ru.common_network.EmptyRequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface ProfileApi {

    @POST("http://cy37212.tmweb.ru/Android/lk.php")
    fun get(
            @Body body: EmptyRequestBody = EmptyRequestBody
    ): Single<List<Any>>

}