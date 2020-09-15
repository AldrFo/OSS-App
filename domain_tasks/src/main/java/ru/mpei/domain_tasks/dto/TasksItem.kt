package ru.mpei.domain_tasks.dto

import com.google.gson.annotations.SerializedName

data class TasksItem(

        @SerializedName("description_short")
        var taskName: String,

        @SerializedName("description_full")
        var taskDescription: String,

        @SerializedName("place")
        var location: String,

        @SerializedName("reward")
        var price: String,

        @SerializedName("start")
        var startDate: String,

        @SerializedName("end")
        var endDate: String,

        @SerializedName("refuse_before")
        var refuseInfo: String,

        var status: String? = null,

        var changeBefore: String? = null,

        @SerializedName("id_task")
        var id: String? = null,

        var penalty: String? = null
)