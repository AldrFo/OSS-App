package ru.mpei.domain_profile

import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import ru.mpei.domain_profile.dto.*

class ProfileRepository (
        private val profileApi: ProfileApi
){

    fun authorize(id: String, pass: String): Single<ProfileItem> = profileApi.authorize(id, pass)

    fun authenticate(email: String, pass: String): Single<ParamsItem> = profileApi.authenticate(email, pass)

    fun loadTasks(type: String, id: String): Single<List<TaskItem>> = profileApi.loadTasks(type, id)

    fun confirmTask(body: ConfirmRefuseItem): Single<ResponseBody> = profileApi.confirmTask(body = body)

    fun sendReport(body: ReportItem, image: MultipartBody.Part?): Single<ResponseBody> = profileApi.sendReport(
        comment =  RequestBody.create("multipart/form-data".toMediaTypeOrNull(), body.comment),
        task_id = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), body.task_id),
        user_id = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), body.user_id),
        file_name = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), body.file_name),
        image = image)

    fun refuseTask(body: ConfirmRefuseItem): Single<ResponseBody> = profileApi.refuseTask(body = body)
}