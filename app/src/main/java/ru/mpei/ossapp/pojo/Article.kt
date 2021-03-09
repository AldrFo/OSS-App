package ru.mpei.ossapp.pojo

import android.media.Image
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Article(
    @SerializedName("id")
    @Expose
    var id: String,

    @SerializedName("name")
    @Expose
    var name: String,

    var img: Image?,

    @SerializedName("chislo")
    @Expose
    var chislo: String,

    @SerializedName("month")
    @Expose
    var month: String,

    @SerializedName("hour")
    @Expose
    var hour: String,

    @SerializedName("content")
    @Expose
    var content: String,

    @SerializedName("image_src")
    @Expose
    var imageSrc: String
)