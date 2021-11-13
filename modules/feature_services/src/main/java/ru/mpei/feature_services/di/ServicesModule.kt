package ru.mpei.feature_services.di

/**
 * Андрей Турлюк
 * А-08-17
 */

import kekmech.ru.common_di.ModuleProvider
import ru.mpei.feature_services.mvi.ServicesFeatureFactory
import org.koin.dsl.bind
import ru.mpei.feature_services.mvi.ServicesActor

// Это класс объявления бинов, которые будут доступны из всех мест кода
object ServicesModule: ModuleProvider({
    single { ServicesActor() } bind ServicesActor::class

    factory { ServicesFeatureFactory(get()) } bind ServicesFeatureFactory::class
})