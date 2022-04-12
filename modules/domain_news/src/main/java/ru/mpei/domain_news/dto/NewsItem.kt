package ru.mpei.domain_news.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NewsItem(
    var id: String = "",
    var date : String = "",
    var name: String = "",
    var chislo: String = "",
    var month: String = "",
    val month_num: Int = when (month){
        "января" -> 1
        "февраля" -> 2
        "марта" -> 3
        "апреля" -> 4
        "мая" -> 5
        "июня" -> 6
        "июля" -> 7
        "августа" -> 8
        "сентября" -> 9
        "октября" -> 10
        "ноября" -> 11
        else -> 12
    },
    var hour: String = "",
    var describtion: String = "",
    var content: String = "",
    @SerializedName("image_src")
    var imageUrl: String = ""
) : Serializable {
    companion object{
        fun getMonthNameFromNum(num : Int) : String = when (num){
            1 -> "января"
            2 -> "февраля"
            3 -> "марта"
            4 -> "апреля"
            5 -> "мая"
            6 -> "июня"
            7 -> "июля"
            8 -> "августа"
            9 -> "сентября"
            10 -> "октября"
            11 -> "ноября"
            12 -> "декабря"
            else -> "неизвестно"
        }
    }
}