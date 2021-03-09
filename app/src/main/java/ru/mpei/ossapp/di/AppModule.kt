package ru.mpei.ossapp.di

import android.content.Context
import android.content.SharedPreferences
import kekmech.ru.common_di.ModuleProvider
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.bind
import java.util.*

object AppModule : ModuleProvider({
    single { Locale.GERMAN } bind Locale::class
    single { androidApplication().getSharedPreferences("settings", Context.MODE_PRIVATE) } bind SharedPreferences::class
})