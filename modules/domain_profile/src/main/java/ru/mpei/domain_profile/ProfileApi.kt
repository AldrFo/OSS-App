package ru.mpei.domain_profile

import io.reactivex.Single
import kekmech.ru.common_annotations.EndpointUrl
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import ru.mpei.domain_profile.dto.ParamsItem
import ru.mpei.domain_profile.dto.ProfileItem
import ru.mpei.domain_profile.dto.TaskItem

@EndpointUrl("http://oss-mpei.ru/")
interface ProfileApi {

    @GET("Android/lk.php")
    fun authorize(@Query("id") id: String, @Query("pass") pass: String): Single<ProfileItem>

    @GET("Android/auth.php")
    fun authenticate(@Query("email") email: String, @Query("password") password: String): Single<ParamsItem>

    @GET("Android/get_tasks.php")
    fun loadTasks(@Query("type") type: String, @Query("id") id: String): Single<List<TaskItem>>

    @FormUrlEncoded
    @POST("Android/restore_pass.php")
    fun restorePass(@Field("email") email: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("Android/register.php")
    fun register(
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("surname") surname: String,
        @Field("gender") gender: String,
        @Field("group") group: String,
        @Field("password") password: String
    ): Call<ResponseBody>

    @POST("Android/edit_report.php")
    @FormUrlEncoded
    fun sendReport(
        @Field("task_id") taskId: String,
        @Field("user_id") userId: String,
        @Field("comment") comment: String,
        @Field("file_name") fileName: String
    ): Single<ResponseBody>

    @POST("Android/confirm_task.php")
    @FormUrlEncoded
    fun confirmTask(
        @Field("task_id") taskId: String,
        @Field("user_id") userId: String
    ): Single<ResponseBody>

}