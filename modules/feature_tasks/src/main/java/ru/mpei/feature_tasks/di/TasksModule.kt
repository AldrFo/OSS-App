package ru.mpei.feature_tasks.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.common_network.retrofit.buildApi
import org.koin.dsl.bind
import retrofit2.Retrofit
import ru.mpei.domain_tasks.TasksApi
import ru.mpei.domain_tasks.TasksRepository
import ru.mpei.feature_tasks.mvi.TasksActor
import ru.mpei.feature_tasks.mvi.TasksFeatureFactory

// Это класс объявления бинов, которые будут доступны из всех мест кода
object TasksModule : ModuleProvider({
    single { get<Retrofit.Builder>().buildApi<TasksApi>() } bind TasksApi::class
    single { TasksRepository(get()) } bind TasksRepository::class
    single { TasksActor(get()) } bind TasksActor::class

    factory { TasksFeatureFactory(get()) } bind TasksFeatureFactory::class
})