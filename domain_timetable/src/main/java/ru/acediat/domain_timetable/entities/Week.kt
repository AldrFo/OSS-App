package ru.acediat.domain_timetable.entities

import ru.acediat.domain_timetable.DatePicker
import ru.acediat.domain_timetable.items.LessonItem

class Week(
    lessonList : List<LessonItem>
) {

    val days = DatePicker.getCurrentWeekDays()

    private val dayNames = arrayOf("ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС")
    private val monthNames =
        arrayOf("янв", "фев", "мар", "апр", "май", "июн", "июл", "авг", "сен", "окт", "ноя", "дек")
    private val weekTimetable = ArrayList<ArrayList<LessonItem>>()

    init{
        var day = lessonList[0].dayOfWeek
        val dayTimetable = ArrayList<LessonItem>()
        for(lesson in lessonList){
            if(lesson.dayOfWeek == day){
                dayTimetable.add(lesson)
            }else{
                addDayToWeekTimetable(dayTimetable)
                dayTimetable.add(lesson)
                day = lesson.dayOfWeek
            }
        }
        addDayToWeekTimetable(dayTimetable)
        while(weekTimetable.size <= 7)
            weekTimetable.add(arrayListOf())
    }

    fun getDayTimetable(dayNumber : Int) : ArrayList<LessonItem> = weekTimetable[dayNumber]

    fun getTimetableFormatDates(dayNumber : Int) : String
        = "${getDayName(dayNumber)}\n${days[dayNumber].date}"

    private fun getDayName(dayNumber : Int) : String = dayNames[dayNumber]

    private fun getMonthName(monthNumber : Int) : String = monthNames[monthNumber]

    private fun addDayToWeekTimetable(day : ArrayList<LessonItem>){
        weekTimetable.add(day.clone() as ArrayList<LessonItem>)
        day.clear()
    }

}