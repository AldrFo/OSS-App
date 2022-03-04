package ru.acediat.feature_timetable.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import ru.acediat.feature_timetable.R

class DayTabsLayout : TabLayout {

    constructor(context : Context)
        : super(context){
        init()
    }

    constructor(context: Context, attrs : AttributeSet)
        : super(context, attrs){
        init()
    }

    constructor(context : Context, attrs : AttributeSet, defStyleAttr : Int)
        : super(context, attrs, defStyleAttr){
        init()
    }

    fun setDates(vararg dates : Int, mondayMonth : Int){

    }

    private fun init(){
        addOnTabSelectedListener(object : OnTabSelectedListener{
            override fun onTabSelected(tab: Tab?) { setTabTextSelected(tab, true) }

            override fun onTabUnselected(tab: Tab?) { setTabTextSelected(tab, false) }

            override fun onTabReselected(tab: Tab?) {}
        })
    }

    private fun setTabTextSelected(tab : Tab?, _isSelected : Boolean){
        if(tab != null && tab.customView != null){
            val tabText = tab.customView!!.findViewById<TextView>(R.id.tabLayoutText)
            with(tabText){
                setTextColor(resources.getColor(if(_isSelected) R.color.black else R.color.text_gray))
                isSelected = _isSelected
            }
        }
    }
}