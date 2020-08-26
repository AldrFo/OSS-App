package ru.mpei.domain_events

import io.reactivex.Single
import kekmech.ru.common_network.EmptyRequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface EventsApi {

    @POST("http://cy37212.tmweb.ru/Android/afisha.php")
    fun get(
        @Body body: EmptyRequestBody = EmptyRequestBody
    ): Single<List<Any>>

}