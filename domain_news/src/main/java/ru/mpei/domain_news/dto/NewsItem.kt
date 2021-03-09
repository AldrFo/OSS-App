package ru.mpei.domain_news.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NewsItem(
    var id: String = "",
    var name: String = "",
    var chislo: String = "",
    var month: String = "",
    var hour: String = "",
    var describtion: String = "",
    var content: String = "",
    @SerializedName("image_src")
    var imageUrl: String = ""
) : Serializable