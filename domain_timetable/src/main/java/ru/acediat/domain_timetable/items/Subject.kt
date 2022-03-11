package ru.acediat.domain_timetable.items

import android.graphics.drawable.Drawable

data class Subject(
    val time : String,
    val type : String,
    val name : String,
    val place : String,
    val teacherName : String,
    val indicatorBackground : Drawable
)
