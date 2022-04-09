package ru.acediat.feature_timetable.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import ru.acediat.domain_timetable.entities.Week
import ru.acediat.feature_timetable.R

class DaysAdapter(
    private val context : Context,
    private val week : Week
) : PagerAdapter() {

    override fun getCount(): Int = 7

    override fun getPageTitle(position: Int): CharSequence = week.getTimetableFormatDates(position)

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val timetable = week.getDayTimetable(position)
        if(timetable.size != 0) {
            with(RecyclerView(context)) {
                layoutManager = LinearLayoutManager(context)
                adapter = LessonsAdapter(context, timetable)
                container.addView(this)
                return@instantiateItem this
            }
        }else{
            with(LayoutInflater.from(context)
                .inflate(R.layout.item_relax, container, false)) {
                container.addView(this)
                return@instantiateItem this
            }
        }
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}