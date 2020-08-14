package ru.mpei.vmss.myapplication.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import ru.mpei.vmss.myapplication.R
import ru.mpei.vmss.myapplication.fragments.Articles
import ru.mpei.vmss.myapplication.fragments.Others
import ru.mpei.vmss.myapplication.fragments.Tasks
import ru.mpei.vmss.myapplication.fragments.User

class MainActivity : AppCompatActivity() {
    private var prevMenuItem: MenuItem? = null
    private val activity: Activity = this

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelected)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val supportFragmentManger = supportFragmentManager

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

        val adapter = MyAdapter(supportFragmentManger)

        view_pager.adapter = adapter
        view_pager.offscreenPageLimit = 5
        view_pager.currentItem = 0

        view_pager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                hideKeyboard(activity)
            }

            override fun onPageSelected(position: Int) {
                if (prevMenuItem != null) prevMenuItem!!.isChecked = false else navigation.menu.getItem(0).isChecked = false
                navigation.menu.getItem(position).isChecked = true
                prevMenuItem = navigation.menu.getItem(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    override fun onPause() {
        super.onPause()
        saveData()
    }

    override fun onResume() {
        super.onResume()
        data
    }

    private val mOnNavigationItemSelected = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.navigation_dashboard -> {
                view_pager.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_news -> {
                view_pager.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                view_pager.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_tasks -> {
                view_pager.currentItem = 3
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_others -> {
                view_pager.currentItem = 4
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    class MyAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val dashboardFragment: Articles = Articles(0)
        private val newsFragment: Articles = Articles(1)
        private val userFragment: User = User()
        private val othersFragment: Others
        override fun getCount(): Int {
            return 5
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                1 -> newsFragment
                2 -> userFragment
                3 -> tasksFragment
                4 -> othersFragment
                else -> dashboardFragment
            }
        }

        companion object {
            lateinit var tasksFragment: Tasks
        }

        init {
            tasksFragment = Tasks()
            othersFragment = Others()
        }
    }

    private fun saveData() {
        val editor = mSettings!!.edit()
        editor.putBoolean(APP_PREFERENCES_FLAG, User.isAuth)
        editor.putString(APP_PREFERENCES_ID, User.userId)
        editor.putString(APP_PREFERENCES_PASS, User.hashPass)
        editor.apply()
    }

    companion object {
        const val APP_PREFERENCES = "settings"
        const val APP_PREFERENCES_FLAG = "isAuth"
        const val APP_PREFERENCES_PASS = "hashCode"
        const val APP_PREFERENCES_ID = "userId"
        private var mSettings: SharedPreferences? = null
        @JvmStatic
        fun hideKeyboard(activity: Activity) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        val data: Unit
            get() {
                User.userId = mSettings!!.getString(APP_PREFERENCES_ID, "")
                User.hashPass = mSettings!!.getString(APP_PREFERENCES_PASS, "")
                User.isAuth = mSettings!!.getBoolean(APP_PREFERENCES_FLAG, false)
            }

        @JvmStatic
        fun deleteData() {
            User.userId = ""
            User.hashPass = ""
            User.isAuth = false
            val editor = mSettings!!.edit()
            editor.clear().apply()
        }
    }
}