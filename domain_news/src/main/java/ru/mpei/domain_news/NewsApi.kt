package ru.mpei.domain_news

import io.reactivex.Single
import kekmech.ru.common_annotations.EndpointUrl
import retrofit2.http.GET
import ru.mpei.domain_news.dto.NewsItem

@EndpointUrl("http://cy37212.tmweb.ru/")
interface NewsApi {

    @GET("Android/news.php")
    fun get(): Single<List<NewsItem>>
}