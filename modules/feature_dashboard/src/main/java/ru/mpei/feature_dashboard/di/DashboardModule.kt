package ru.mpei.feature_dashboard.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.common_network.retrofit.buildApi
import org.koin.dsl.bind
import retrofit2.Retrofit
import ru.mpei.domain_news.NewsApi
import ru.mpei.domain_news.NewsRepository
import ru.mpei.feature_dashboard.mvi.DashboardActor
import ru.mpei.feature_dashboard.mvi.DashboardFeatureFactory

object DashboardModule : ModuleProvider({
    single { get<Retrofit.Builder>().buildApi<NewsApi>() } bind NewsApi::class
    single { NewsRepository(get()) } bind NewsRepository::class
    single { DashboardActor(get()) } bind DashboardActor::class

    factory { DashboardFeatureFactory(get()) } bind DashboardFeatureFactory::class
})