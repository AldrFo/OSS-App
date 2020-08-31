package ru.mpei.ossapp.ui.main.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.common_navigation.di.MainFragmentHolder
import ru.mpei.ossapp.ui.main.BottomTabsSwitcher
import ru.mpei.ossapp.ui.main.BottomTabsSwitcherImpl
import kekmech.ru.mpeiapp.ui.main.di.MainScreenDependencies
import ru.mpei.ossapp.ui.main.MainFragment
import org.koin.dsl.bind

object MainScreenModule : ModuleProvider({
    single { BottomTabsSwitcherImpl } bind BottomTabsSwitcher::class
    single { MainScreenDependencies(get()) } bind MainScreenDependencies::class

    val mainFragmentHolder = object : MainFragmentHolder {
        override fun invoke() = MainFragment.newInstance()
    }
    single { mainFragmentHolder } bind MainFragmentHolder::class
})