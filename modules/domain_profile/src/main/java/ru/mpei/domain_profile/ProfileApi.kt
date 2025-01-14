package ru.mpei.domain_profile

import io.reactivex.Single
import kekmech.ru.common_annotations.EndpointUrl
import kekmech.ru.common_kotlin.OSS_URL
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import ru.mpei.domain_profile.dto.*

@EndpointUrl(OSS_URL)
interface ProfileApi {

    @GET("Android/lk.php")
    fun authorize(
        @Query("id") id: String,
        @Query("pass") pass: String
    ): Single<ProfileItem>

    @GET("Android/auth.php")
    fun authenticate(
        @Query("email") email: String,
        @Query("password") password: String
    ): Single<ParamsItem>

    @GET("Android/get_tasks.php")
    fun loadTasks(
        @Query("type") type: String,
        @Query("id") id: String
    ): Single<List<TaskItem>>

    @GET("Android/restore_pass2.php")
    fun restorePass(
        @Query("email") email : String
    ): Call<ResponseBody>

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

    @Multipart
    @POST("Android/edit_report.php")
    fun sendReport(
        @Part("comment") comment: RequestBody,
        @Part("task_id") task_id: RequestBody,
        @Part("user_id") user_id: RequestBody,
        @Part("file_name") file_name: RequestBody,
        @Part image: MultipartBody.Part?
    ): Single<ResponseBody>

    @POST("Android/confirm_task.php")
    fun confirmTask(
        @Body body: ConfirmRefuseItem
    ): Single<ResponseBody>

    @POST("/Android/refuse_task.php")
    fun refuseTask(
        @Body body: ConfirmRefuseItem
    ): Single<ResponseBody>

    @GET("/Android/shop.php")
    fun loadProducts(
        @Query("type") type: String
    ): Single<List<ProductItem>>

    @POST("/Android/shop.php")
    @FormUrlEncoded
    fun buyProduct(
        @Field("userId") userId: String,
        @Field("productId") productId: String
    ): Call<UserShopInfoItem>

    @GET("/Android/shop.php?type=user_shop_info")
    fun getUserShopInfo(
        @Query("id") id: String
    ): Call<UserShopInfoItem>
}