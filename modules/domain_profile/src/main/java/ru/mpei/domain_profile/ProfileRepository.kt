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

    fun confirmTask(body: ConfirmItem): Single<ResponseBody> = profileApi.confirmTask(body = body)

    fun sendReport(body: ReportItem): Single<ResponseBody> = profileApi.sendReport(body = body)
}