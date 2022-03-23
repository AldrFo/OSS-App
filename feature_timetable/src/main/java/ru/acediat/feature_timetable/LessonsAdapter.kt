package ru.acediat.feature_timetable

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.acediat.domain_timetable.items.LessonItem


class LessonsAdapter(
    private val context : Context,
    private val lessonItems : List<LessonItem>
) : RecyclerView.Adapter<LessonsAdapter.ViewHolder>(){

    private val inflater : LayoutInflater = LayoutInflater.from(context)
    private val indicators = arrayOf(R.drawable.shape_indicator_red,
                                     R.drawable.shape_indicator_green,
                                     R.drawable.shape_indicator_yellow,
                                     R.drawable.shape_indicator_blue)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val view = inflater.inflate(R.layout.item_subject, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            time.text = lessonItems[position].time
            type.text = lessonItems[position].type
            name.text = lessonItems[position].name
            place.text = lessonItems[position].place
            teacherName.text = lessonItems[position].teacherName
            indicator.background = ContextCompat.getDrawable(context, indicators[lessonItems[position].indicatorType])
        }
    }

    override fun getItemCount(): Int = lessonItems.size

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val time : TextView
        val type : TextView
        val name : TextView
        val place : TextView
        val teacherName : TextView
        val indicator : View

        init{
            with(itemView){
                time = findViewById(R.id.subjectTime)
                type = findViewById(R.id.subjectType)
                name = findViewById(R.id.subjectName)
                place = findViewById(R.id.subjectPlace)
                teacherName = findViewById(R.id.subjectTeacherName)
                indicator = findViewById(R.id.indicator)
            }
        }
    }
}