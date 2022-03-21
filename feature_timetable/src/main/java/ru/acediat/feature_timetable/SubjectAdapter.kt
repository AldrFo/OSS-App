package ru.acediat.feature_timetable

import ru.acediat.feature_timetable.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.acediat.domain_timetable.items.Subject


class SubjectAdapter(
    context : Context,
    private val subjects : List<Subject>
) : RecyclerView.Adapter<SubjectAdapter.ViewHolder>(){

    private val inflater : LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val view = inflater.inflate(R.layout.item_subject, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            time.text = subjects[position].time
            type.text = subjects[position].type
            name.text = subjects[position].name
            place.text = subjects[position].place
            teacherName.text = subjects[position].teacherName
            indicator.background = subjects[position].indicatorBackground
        }
    }

    override fun getItemCount(): Int = subjects.size

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