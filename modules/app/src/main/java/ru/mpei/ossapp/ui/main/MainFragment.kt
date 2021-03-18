package ru.mpei.ossapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import io.reactivex.disposables.Disposable
import kekmech.ru.common_android.onActivityResult
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.BackButtonListener
import kekmech.ru.mpeiapp.ui.main.di.MainScreenDependencies
import org.koin.android.ext.android.inject
import ru.mpei.ossapp.R
import ru.mpei.ossapp.databinding.FragmentMainBinding
import ru.mpei.ossapp.ui.main.mvi.*

class MainFragment : BaseFragment<MainScreenEvent, MainScreenEffect, MainScreenState, MainScreenFeature>(), BackButtonListener {

    override val initEvent: MainScreenEvent get() = MainScreenEvent.Wish.Init
    override var layoutId: Int = R.layout.fragment_main

    override fun createFeature() = MainScreenFeatureFactory.create()

    private val dependencies by inject<MainScreenDependencies>()
    private var bottomBarController: BottomBarController? = null
    private var tabsSwitcherDisposable: Disposable? = null
    private val tabsSwitcher by fastLazy { dependencies.bottomTabsSwitcher }
    private val binding by viewBinding(FragmentMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            // start observing data
        }
    }

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedInternal(view, savedInstanceState)

        val controller = bottomBarController ?: BottomBarController(this)
        controller.init(this, binding.bottomNavigation)
        bottomBarController = controller
    }

    override fun onResume() {
        super.onResume()
        tabsSwitcherDisposable = tabsSwitcher.observe().subscribe {
            it.value?.let { tab ->
                bottomBarController?.switchTab(tab)
                tabsSwitcher.clearTab()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        tabsSwitcherDisposable?.dispose()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        childFragmentManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed(): Boolean {
        return bottomBarController?.popStack() != true
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}