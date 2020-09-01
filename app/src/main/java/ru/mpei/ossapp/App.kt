package ru.mpei.ossapp

import android.app.Application
import kekmech.ru.common_di.modules
import kekmech.ru.common_navigation.di.NavigationModule
import kekmech.ru.common_network.di.NetworkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.mpei.feature_dashboard.di.DashboardModule
import ru.mpei.ossapp.di.AppModule
import ru.mpei.ossapp.ui.main.di.MainScreenModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
        initKoin()
    }


    private fun initKoin() = startKoin {
        androidLogger()
        androidContext(this@App)
        modules(listOf(
            AppModule,
            NavigationModule,
            MainScreenModule,
            NetworkModule,
            DashboardModule
        ))
    }


    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            //Timber.plant(DebugTree())
        }
    }
}