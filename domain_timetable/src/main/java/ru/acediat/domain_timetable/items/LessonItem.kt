package ru.acediat.domain_timetable.items

data class LessonItem(
    val time : String,
    val type : String,
    val dayOfWeek: Int,
    val indicatorType : Int,
    val name : String,
    val place : String,
    val teacherName : String,
    val lessonNumber : Int,
)
