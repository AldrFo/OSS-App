package ru.mpei.ossapp

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.viewbinding.BuildConfig
import kekmech.ru.common_di.modules
import kekmech.ru.common_navigation.di.NavigationModule
import kekmech.ru.common_network.di.NetworkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.mpei.feature_tasks.di.TasksModule
import ru.mpei.feature_dashboard.di.DashboardModule
import ru.mpei.feature_profile.di.ProfileModule
import ru.mpei.feature_services.di.ServicesModule
import ru.mpei.ossapp.di.AppModule
import ru.mpei.ossapp.ui.main.di.MainScreenModule
import timber.log.Timber

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
            DashboardModule,
            TasksModule,
            ProfileModule,
            ServicesModule
        ))
    }


    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}