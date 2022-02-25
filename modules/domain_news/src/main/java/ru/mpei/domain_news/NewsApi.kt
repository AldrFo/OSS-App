package ru.mpei.domain_news

import io.reactivex.Single
import kekmech.ru.common_annotations.EndpointUrl
import kekmech.ru.common_kotlin.OSS_AFISHA
import kekmech.ru.common_kotlin.OSS_NEWS
import kekmech.ru.common_kotlin.OSS_URL
import retrofit2.http.GET
import ru.mpei.domain_news.dto.NewsItem

@EndpointUrl(OSS_URL)
interface NewsApi {

    @GET(OSS_NEWS)
    fun getNews(): Single<List<NewsItem>>

    @GET(OSS_AFISHA)
    fun getEvents(): Single<List<NewsItem>>
}