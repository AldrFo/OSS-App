package ru.mpei.domain_profile

import io.reactivex.Single
import okhttp3.ResponseBody
import ru.mpei.domain_profile.dto.*

class ProfileRepository (
        private val profileApi: ProfileApi
){

    fun authorize(id: String, pass: String): Single<ProfileItem> = profileApi.authorize(id, pass)

    fun authenticate(email: String, pass: String): Single<ParamsItem> = profileApi.authenticate(email, pass)

    fun loadTasks(type: String, id: String): Single<List<TaskItem>> = profileApi.loadTasks(type, id)

    fun confirmTask(taskId: String, userId: String): Single<ResponseBody> = profileApi.confirmTask(taskId, userId)

    fun sendReport(taskId: String, userId: String, comment: String, fileName: String): Single<ResponseBody> = profileApi.sendReport(taskId = taskId, userId = userId, comment = comment, fileName = fileName)
}