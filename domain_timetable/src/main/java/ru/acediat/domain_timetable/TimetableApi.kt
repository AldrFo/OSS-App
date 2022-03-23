package ru.acediat.domain_timetable

import io.reactivex.Observable
import kekmech.ru.common_annotations.EndpointUrl
import kekmech.ru.common_kotlin.OSS_URL
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@EndpointUrl(OSS_URL)
interface TimetableApi {

    @GET("Android/get_group_timetable.php")
    fun getPersonLessons(
        @Query("group") group : String,
        @Query("fromDate") fromDate : String,
        @Query("toDate") toDate : String,
    ) : Observable<List<LessonsDTO>>

}