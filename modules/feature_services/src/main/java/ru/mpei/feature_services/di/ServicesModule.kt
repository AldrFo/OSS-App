package ru.mpei.feature_services.di

import kekmech.ru.common_di.ModuleProvider
import ru.mpei.feature_services.mvi.ServicesFeatureFactory
import org.koin.dsl.bind
import ru.mpei.feature_services.mvi.ServicesActor

object ServicesModule: ModuleProvider({
    single { ServicesActor() } bind ServicesActor::class

    factory { ServicesFeatureFactory(get()) } bind ServicesFeatureFactory::class
})