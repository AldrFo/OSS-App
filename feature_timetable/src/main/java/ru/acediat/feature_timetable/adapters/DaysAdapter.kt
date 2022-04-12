package ru.acediat.feature_timetable.adapters

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import kekmech.ru.common_kotlin.OSS_TAG
import ru.acediat.domain_timetable.entities.Week
import ru.acediat.domain_timetable.items.LessonItem
import ru.acediat.feature_timetable.R

class DaysAdapter(
    private val context : Context,
    private val preferences: SharedPreferences
) : PagerAdapter() {

    private var week : Week? = null

    fun setWeek(week : Week){
        this.week = week
        notifyDataSetChanged()
    }

    override fun getCount(): Int = 7

    override fun getPageTitle(position: Int): CharSequence
        = week?.getTimetableFormatDates(position) ?: "..."

    override fun instantiateItem(container: ViewGroup, position: Int) : Any {
        Log.d(OSS_TAG, "item $position inst")
        if(week == null && isGroupUnknown()) {
            with(getUnknownGroupView(container)){
                container.addView(this)
                return@instantiateItem this
            }
        }else if(week == null){
            return container
        }

        val timetable = week!!.getDayTimetable(position)
        if(timetable.size != 0) {
            with(getTimetableView(timetable)){
                container.addView(this)
                return@instantiateItem this
            }
        }else{
            with(getRelaxView(container)){
                container.addView(this)
                return@instantiateItem this
            }
        }
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) =
        container.removeView(`object` as View)

    override fun getItemPosition(`object`: Any): Int = POSITION_NONE

    private fun getTimetableView(timetable : ArrayList<LessonItem>): RecyclerView =
        with(RecyclerView(context)) {
            layoutManager = LinearLayoutManager(context)
            adapter = LessonsAdapter(context, timetable)
            return@getTimetableView this
        }

    private fun getRelaxView(container : ViewGroup) : View =
        LayoutInflater.from(context).inflate(R.layout.item_relax, container, false)

    private fun getUnknownGroupView(container : ViewGroup) : View =
        LayoutInflater.from(context).inflate(R.layout.item_unknown_group, container, false)

    private fun isGroupUnknown() =
        !(preferences.getBoolean("isAuth", false) || preferences.getString("timetableGroup", "") ?: "" != "")
}