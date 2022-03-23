package ru.acediat.feature_timetable

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import ru.acediat.domain_timetable.Week

class DaysAdapter(
    private val context : Context,
    private val week : Week
) : PagerAdapter() {

    override fun getCount(): Int = 7

    override fun getPageTitle(position: Int): CharSequence? = week.getTimetableFormatDates(position)

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val recyclerView = RecyclerView(context)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = LessonsAdapter(context, week.getDayTimetable(position))
        container.addView(recyclerView)
        return recyclerView
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RecyclerView)
    }
}