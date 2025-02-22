package ru.mpei.ossapp.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kekmech.ru.common_navigation.BottomTab
import ru.acediat.feature_timetable.TimetableFragment
import ru.mpei.feature_tasks.TasksFragment
import ru.mpei.feature_dashboard.DashboardFragment
import ru.mpei.feature_profile.ProfileFragment
import ru.mpei.feature_services.ServicesFragment
import ru.mpei.ossapp.R
import java.util.concurrent.TimeUnit

class BottomBarController(fragment: Fragment) {

    private val childFragmentManager: FragmentManager = fragment.childFragmentManager
    private var lastSelectedTab = BottomTab.DASHBOARD
    private var bottomNavView: BottomNavigationView? = null
    private val currentTabFragment: Fragment?
        get() = childFragmentManager.fragments.firstOrNull { !it.isHidden && it.tag != null }
    private val backStack: BottomBarBackStack = BottomBarBackStack(firstTab = BottomTab.DASHBOARD)

    private val navSelectListener = BottomNavigationView.OnNavigationItemSelectedListener {  item ->
        val tab = when (item.itemId) {
            R.id.navigation_timetable -> BottomTab.TIMETABLE
            R.id.navigation_dashboard -> BottomTab.DASHBOARD
            R.id.navigation_profile -> BottomTab.PROFILE
            R.id.navigation_tasks -> BottomTab.TASKS
            R.id.navigation_services -> BottomTab.SERVICES
            else -> null
        }
        tab?.let {
            selectTab(it)
            backStack.push(it)
        }
        true
    }

    fun init(
        containerFragment: Fragment,
        bottomNavView: BottomNavigationView
    ) {
        this.bottomNavView = bottomNavView
        bottomNavView.setOnNavigationItemSelectedListener(navSelectListener)
        selectTab(lastSelectedTab)
        backStack.push(lastSelectedTab)
        if (bottomNavView.selectedItemId == R.id.navigation_dashboard) {
            containerFragment.postponeEnterTransition(300L, TimeUnit.MILLISECONDS)
        }
    }

    fun switchTab(tab: BottomTab) { bottomNavView?.let { it.selectedItemId = getItemByTab(tab) } }

    fun popStack(): Boolean = backStack.popAndPeek()?.let { switchTab(it); true } ?: false

    private fun selectTab(tab: BottomTab) {
        val currentFragment = currentTabFragment
        val newFragment = childFragmentManager.findFragmentByTag(tab.name)

        if (currentFragment != null && newFragment != null && currentFragment == newFragment) return

        if(newFragment == null) {
            childFragmentManager.beginTransaction().apply {
                add(R.id.fragmentContainer, createTabFragment(tab), tab.name)
            }.commitNow()
        }

        childFragmentManager.beginTransaction().apply {
            currentFragment?.let { fragment ->
                hide(fragment)
                fragment.userVisibleHint = false
            }
            newFragment?.let { fragment ->
                show(fragment)
                fragment.userVisibleHint = true
            }
        }.commitNow()
        lastSelectedTab = tab
    }

    private fun getItemByTab(tab: BottomTab) = when (tab) {
        BottomTab.TIMETABLE -> R.id.navigation_timetable
        BottomTab.DASHBOARD -> R.id.navigation_dashboard
        BottomTab.PROFILE -> R.id.navigation_profile
        BottomTab.TASKS -> R.id.navigation_tasks
        BottomTab.SERVICES -> R.id.navigation_services
    }

    private fun createTabFragment(tab: BottomTab) : Fragment = when (tab) {
        BottomTab.TIMETABLE -> TimetableFragment()
        BottomTab.DASHBOARD -> DashboardFragment()
        BottomTab.PROFILE -> ProfileFragment()
        BottomTab.TASKS ->  TasksFragment()
        BottomTab.SERVICES -> ServicesFragment()
    }
}