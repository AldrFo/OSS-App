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
        val dayNumbers = getDayNumbers(lessonList)
        for(i in 1..7){
            if(dayNumbers.contains(i)){
                addDayToWeekTimetable(getDayLessons(lessonList, i))
            }else{
                addDayToWeekTimetable(arrayListOf())
            }
        }
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

    private fun getDayLessons(lessonList: List<LessonItem>, day : Int) : ArrayList<LessonItem> {
        val lessons = ArrayList<LessonItem>()
        for(lesson in lessonList){
            if(lesson.dayOfWeek == day)
                lessons.add(lesson)
        }
        return lessons
    }

    private fun getDayNumbers(lessonList : List<LessonItem>) : ArrayList<Int>{
        if(lessonList.isEmpty())
            return arrayListOf(0)

        val days = arrayListOf<Int>()
        for(lesson in lessonList){
            if(!days.contains(lesson.dayOfWeek))
                days.add(lesson.dayOfWeek)
        }

        return days
    }

}