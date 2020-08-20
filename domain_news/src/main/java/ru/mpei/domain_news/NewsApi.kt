package ru.mpei.domain_news

import io.reactivex.Single
import kekmech.ru.common_network.EmptyRequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface NewsApi {

    @POST("http://cy37212.tmweb.ru/Android/news.php")
    fun get(
        @Body body: EmptyRequestBody = EmptyRequestBody
    ): Single<List<Any>>
}