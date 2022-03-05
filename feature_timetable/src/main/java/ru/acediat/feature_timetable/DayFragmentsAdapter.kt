package ru.acediat.feature_timetable

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class DayFragmentsAdapter(fm : FragmentManager, behavior : Int)
    : FragmentStatePagerAdapter(fm, behavior) {

    override fun getCount(): Int = 7

    override fun getItem(position: Int): Fragment = DayFragment.newInstance()

    override fun getPageTitle(position: Int): CharSequence? = "ПН\n${position}\nмар"
}