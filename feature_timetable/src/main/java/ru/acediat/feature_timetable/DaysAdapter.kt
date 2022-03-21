package ru.acediat.feature_timetable

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import ru.acediat.domain_timetable.items.Subject

class DaysAdapter(
    private val context : Context
) : PagerAdapter() {

    override fun getCount(): Int = 7

    override fun getPageTitle(position: Int): CharSequence? = "ПН\n${position}\nмар"

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val recyclerView = RecyclerView(context)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val subjectsList = arrayListOf(//затычка
            Subject("9:20 - 10:55", "Лекция","Математический анализ","Б-202", "А.Я. Ебу", ContextCompat.getDrawable(context, R.drawable.shape_indicator_red)!!),
            Subject("11:10 - 12:45", "Семинар","Математический анализ","Б-202", "А.Я. Ебу", ContextCompat.getDrawable(context, R.drawable.shape_indicator_green)!! ),
            Subject("13:45 - 15:20", "Лабораторная","Математический анализ","Б-202", "А.Я. Ебу", ContextCompat.getDrawable(context, R.drawable.shape_indicator_yellow)!! )

        )
        recyclerView.adapter = SubjectAdapter(context, subjectsList)
        container.addView(recyclerView)
        return recyclerView
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RecyclerView)
    }
}