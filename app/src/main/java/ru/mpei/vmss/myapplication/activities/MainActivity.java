package ru.mpei.vmss.myapplication.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import ru.mpei.vmss.myapplication.R;
import ru.mpei.vmss.myapplication.fragments.Articles;
import ru.mpei.vmss.myapplication.fragments.Others;
import ru.mpei.vmss.myapplication.fragments.Tasks;
import ru.mpei.vmss.myapplication.fragments.User;

import static ru.mpei.vmss.myapplication.fragments.User.hashPass;
import static ru.mpei.vmss.myapplication.fragments.User.isAuth;
import static ru.mpei.vmss.myapplication.fragments.User.userId;

public class MainActivity extends AppCompatActivity {

    private static ViewPager viewPager;
    private BottomNavigationView navigation;
    private MenuItem prevMenuItem;
    private Activity activity = this;
    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_FLAG = "isAuth";
    public static final String APP_PREFERENCES_PASS = "hashCode";
    public static final String APP_PREFERENCES_ID = "userId";
    private static SharedPreferences mSettings;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        FragmentManager supportFragmentManger = getSupportFragmentManager();
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelected);

        MyAdapter adapter = new MyAdapter(supportFragmentManger);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(5);
        viewPager.setCurrentItem(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                hideKeyboard(activity);
            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null)
                    prevMenuItem.setChecked(false);
                else
                     navigation.getMenu().getItem(0).setChecked(false);

                navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = navigation.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelected = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch(menuItem.getItemId()) {
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_news:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_profile:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_tasks:
                    viewPager.setCurrentItem(3);
                    return true;
                case R.id.navigation_others:
                    viewPager.setCurrentItem(4);
                    return true;
            }
            return false;
        }
    };

    public static class MyAdapter extends FragmentPagerAdapter {

        private Articles dashboardFragment;
        private Articles newsFragment;
        private User userFragment;
        public static Tasks tasksFragment;
        private Others othersFragment;


        MyAdapter(@NonNull FragmentManager fm) {
            super(fm);
            dashboardFragment = new Articles(0);
            newsFragment = new Articles(1);
            userFragment = new User();
            tasksFragment = new Tasks();
            othersFragment = new Others();
        }

        @Override
        public int getCount() {
            return 5;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return newsFragment;
                case 2:
                    return userFragment;
                case 3:
                    return tasksFragment;
                case 4:
                    return othersFragment;
                default:
                    return dashboardFragment;
            }
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void saveData(){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_FLAG, isAuth);
        editor.putString(APP_PREFERENCES_ID, userId);
        editor.putString(APP_PREFERENCES_PASS, hashPass);
        editor.apply();
    }

    public static void getData(){
        userId = mSettings.getString(APP_PREFERENCES_ID, "");
        hashPass = mSettings.getString(APP_PREFERENCES_PASS, "");
        isAuth = mSettings.getBoolean(APP_PREFERENCES_FLAG, false);
    }

    public static void deleteData(){
        userId = "";
        hashPass = "";
        isAuth = false;
        SharedPreferences.Editor editor = mSettings.edit();
        editor.clear().apply();
    }

}
