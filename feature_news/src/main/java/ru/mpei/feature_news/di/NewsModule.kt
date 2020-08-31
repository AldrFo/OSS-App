package ru.mpei.feature_news.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.common_network.retrofit.buildApi
import org.koin.dsl.bind
import retrofit2.Retrofit
import ru.mpei.domain_news.NewsApi
import ru.mpei.domain_news.NewsRepository
import ru.mpei.feature_news.mvi.NewsActor
import ru.mpei.feature_news.mvi.NewsFeatureFactory

object NewsModule : ModuleProvider({
    single { get<Retrofit.Builder>().buildApi<NewsApi>() } bind NewsApi::class
    single { NewsRepository(get()) } bind NewsRepository::class
    single { NewsActor(get()) } bind NewsActor::class

    factory { NewsFeatureFactory(get()) } bind NewsFeatureFactory::class
})