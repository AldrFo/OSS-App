package ru.acediat.domain_timetable

import ru.acediat.domain_timetable.items.LessonItem

class LessonsCreator {

    private val PRACTICE = "Практическое занятие"
    private val LECTURE = "Лекция"
    private val LAB = "Лабораторная работа"

    fun createLessonFromDTO(lessonsDTO: LessonsDTO) : LessonItem {
        with(lessonsDTO) {
            val time = "$beginLesson - $endLesson"
            val type = when(kindOfWork){
                PRACTICE -> "Семинар"
                LECTURE -> "Лекция"
                LAB -> "Лабораторная"
                else -> "Другое"
            }
            val name = discipline
            val dayOfWeek = dayOfWeek
            val teacherName = if(lecturer != "!Не определена !Вакансия") lecturer else ""
            val lessonNumber = lessonNumberStart
            val place = auditorium
            val indicator = when(kindOfWork){
                 PRACTICE -> INDICATOR_GREEN
                 LECTURE -> INDICATOR_RED
                 LAB -> INDICATOR_YELLOW
                else -> INDICATOR_BLUE
            }
            return LessonItem(time, type, dayOfWeek, indicator, name, place, teacherName, lessonNumber)
        }
    }

    companion object{
        val INDICATOR_RED = 0
        val INDICATOR_GREEN = 1
        val INDICATOR_YELLOW = 2
        val INDICATOR_BLUE = 3
    }
}