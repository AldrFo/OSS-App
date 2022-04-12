package ru.acediat.feature_timetable.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.common_network.retrofit.buildApi
import org.koin.dsl.bind
import retrofit2.Retrofit
import ru.acediat.domain_timetable.TimetableApi
import ru.acediat.domain_timetable.TimetableRepository

object TimetableModule : ModuleProvider({
    single{ get<Retrofit.Builder>().buildApi<TimetableApi>() } bind TimetableApi::class
    single{ TimetableRepository(get()) } bind TimetableRepository::class
})