package ru.acediat.domain_timetable

import io.reactivex.Observable
import kekmech.ru.common_annotations.EndpointUrl
import kekmech.ru.common_kotlin.OSS_URL
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//@EndpointUrl("http://ruz.public.mpei.local/RUZService/ruzservice.svc/")
@EndpointUrl(OSS_URL)
interface TimetableApi {

    @GET("lk/shedule.php")
    fun getPersonLessons(
        @Query("fromdate") fromDate : String,
        @Query("todate") toDate : String,
        @Query("groupoid") groupOId : Int
    ) : Observable<List<LessonsDTO>>

    companion object{
        val RECEIVER_TYPE_STUDENT = 0
        val RECEIVER_TYPE_TEACHER = 1
        val RECEIVER_TYPE_ROOM = 2
        val RECEIVER_TYPE_GROUP = 3

        val LANGUAGE_RUSSIAN = 1
        val LANGUAGE_ENGLISH = 2
    }
}