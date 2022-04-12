package ru.acediat.domain_timetable.dto

import com.google.gson.annotations.SerializedName

data class GroupDTO(
    @SerializedName("group")
    val group : String = ""
)
