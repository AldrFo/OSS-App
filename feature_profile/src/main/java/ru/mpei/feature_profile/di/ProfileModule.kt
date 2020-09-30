package ru.mpei.feature_profile.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.common_network.retrofit.buildApi
import org.koin.dsl.bind
import retrofit2.Retrofit
import ru.mpei.domain_profile.ProfileApi
import ru.mpei.domain_profile.ProfileRepository
import ru.mpei.feature_profile.mvi.ProfileActor
import ru.mpei.feature_profile.mvi.ProfileFeatureFactory

object ProfileModule : ModuleProvider({
    single { get<Retrofit.Builder>().buildApi<ProfileApi>() } bind ProfileApi::class
    single { ProfileRepository(get()) } bind ProfileRepository::class
    single { ProfileActor(get()) } bind ProfileActor::class

    factory { ProfileFeatureFactory(get()) } bind ProfileFeatureFactory::class
})