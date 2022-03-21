package ru.mpei.domain_tasks.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "tasks")
data class TasksItem(

        @SerializedName("description_short")
        @ColumnInfo(name = "name")
        var taskName: String,

        @SerializedName("description_full")
        @ColumnInfo(name = "description")
        var taskDescription: String,

        @SerializedName("place")
        @ColumnInfo(name = "place")
        var location: String,

        @SerializedName("reward")
        @ColumnInfo(name = "reward")
        var price: String,

        @SerializedName("start")
        @ColumnInfo(name = "startDate")
        var startDate: String,

        @SerializedName("end")
        @ColumnInfo(name = "endDate")
        var endDate: String,

        @SerializedName("refuse_before")
        @ColumnInfo(name = "refuseInfo")
        var refuseInfo: String,

        var status: String? = null,

        var changeBefore: String? = null,

        @SerializedName("id")
        @ColumnInfo(name = "id")
        @PrimaryKey(autoGenerate = false)
        var id: String = "",

        var penalty: String? = null

): Serializable