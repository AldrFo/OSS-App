package ru.mpei.domain_profile.dto

import com.google.gson.annotations.SerializedName
import org.w3c.dom.Comment
import java.io.Serializable

data class TaskItem(
    @SerializedName("description_short")
    var taskName: String = "",

    @SerializedName("description_full")
    var taskDescription: String = "",

    @SerializedName("place")
    var location: String = "",

    @SerializedName("reward")
    var price: String = "",

    @SerializedName("start")
    var startDate: String = "",

    @SerializedName("end")
    var endDate: String = "",

    @SerializedName("refuse_before")
    var refuseInfo: String = "",

    var status: String = "",

    var changeBefore: String = "",

    @SerializedName("id_task")
    var id: String = "",

    var penalty: String = "",

    var comment: String = ""
): Serializable
