package ru.acediat.domain_timetable

import io.reactivex.Observable
import kekmech.ru.common_annotations.EndpointUrl
import kekmech.ru.common_kotlin.OSS_URL
import retrofit2.http.GET
import retrofit2.http.Query
import ru.acediat.domain_timetable.dto.GroupDTO
import ru.acediat.domain_timetable.dto.GroupValidDTO
import ru.acediat.domain_timetable.dto.LessonsDTO

@EndpointUrl(OSS_URL)
interface TimetableApi {

    @GET("Android/get_group_timetable.php")
    fun getPersonLessons(
        @Query("group") group : String,
        @Query("fromDate") fromDate : String,
        @Query("toDate") toDate : String,
    ) : Observable<List<LessonsDTO>>

    @GET("Android/get_user_group.php")
    fun getUserGroup(
        @Query("userId") userId : Int
    ) : Observable<GroupDTO>

    @GET("Android/is_group_valid.php")
    fun isGroupValid(
        @Query("group") group : String
    ) : Observable<GroupValidDTO>
}