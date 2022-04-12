package ru.acediat.domain_timetable.dto

import com.google.gson.annotations.SerializedName

data class GroupValidDTO(
    @SerializedName("isValid")
    val isGroupValid : Boolean
)