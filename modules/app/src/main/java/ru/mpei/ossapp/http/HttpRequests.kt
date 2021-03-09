package ru.mpei.ossapp.http

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.mpei.ossapp.pojo.Article
import ru.mpei.ossapp.pojo.Task

interface HttpRequests {

    @GET("/Android/afisha.php")
    fun getEvents(): Call<MutableList<Article>>

    @GET("/Android/news.php")
    fun getNews(): Call<MutableList<Article>>

    @GET("/Android/get_tasks.php")
    fun getTasks(@Query("id") id: String?, @Query("type") type: String): Call<MutableList<Task>>

}