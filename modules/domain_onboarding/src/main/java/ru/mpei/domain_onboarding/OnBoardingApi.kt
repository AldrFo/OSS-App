package ru.mpei.domain_onboarding

import io.reactivex.Single
import kekmech.ru.common_network.EmptyRequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface OnBoardingApi {
    @POST("")
    fun get(
            @Body body: EmptyRequestBody = EmptyRequestBody
    ): Single<List<Any>>
}