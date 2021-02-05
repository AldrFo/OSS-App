package ru.mpei.domain_news

import io.reactivex.Single
import ru.mpei.domain_news.dto.NewsItem

class NewsRepository(
    private val newsApi: NewsApi
) {

    fun observeNews(): Single<List<NewsItem>> = newsApi.getNews()
    fun observeEvents(): Single<List<NewsItem>> = newsApi.getEvents()

}