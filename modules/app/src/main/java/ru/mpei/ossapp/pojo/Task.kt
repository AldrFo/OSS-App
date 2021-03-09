package ru.mpei.ossapp.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Task (
    @SerializedName("description_short")
    @Expose
    var taskName: String,

    @SerializedName("description_full")
    @Expose
    var taskDescription: String,

    @SerializedName("place")
    @Expose
    var location: String,

    @SerializedName("reward")
    @Expose
    var price: String,

    @SerializedName("start")
    @Expose
    var startDate: String,

    @SerializedName("end")
    @Expose
    var endDate: String,

    @SerializedName("refuse_before")
    @Expose
    var refuseInfo: String,

    @SerializedName("status")
    @Expose
    var status: String? = null,

    @SerializedName("changeBefore")
    @Expose
    var changeBefore: String? = null,

    @SerializedName("id_task")
    @Expose
    var id: String? = null,

    @SerializedName("penalty")
    @Expose
    var penalty: String? = null
)