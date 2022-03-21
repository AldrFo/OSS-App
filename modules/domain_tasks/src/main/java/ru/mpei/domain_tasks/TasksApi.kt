package ru.mpei.domain_tasks

import io.reactivex.Single
import kekmech.ru.common_annotations.EndpointUrl
import kekmech.ru.common_kotlin.OSS_URL
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.mpei.domain_tasks.dto.TakeTaskItem
import ru.mpei.domain_tasks.dto.TasksItem

const val AVAILABLE = "available"

@EndpointUrl(OSS_URL)
interface TasksApi {

    @GET("Android/get_tasks.php")
    fun get(
        @Query("id") id: String,
        @Query("type") type: String
    ): Single<List<TasksItem>>

    @POST("Android/take_task.php")
    fun take(
        @Body body: TakeTaskItem
    ): Single<ResponseBody>
}