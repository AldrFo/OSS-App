package ru.acediat.domain_timetable

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LessonsDTO(
    @SerializedName("stream")
    val stream : String = "",
    @SerializedName("streamOid")
    val streamOId : Int = 0,
    @SerializedName("stream_facultyoid")
    val streamFacultyOId : Int = 0,
    @SerializedName("group")
    val group : String = "",
    @SerializedName("groupOid")
    val groupOId : Int = 0,
    @SerializedName("group_facultyoid")
    val groupFacultyOId : Int = 0,
    @SerializedName("subGroup")
    val subgroup : String = "",
    @SerializedName("subgroupOid")
    val subgroupOId : Int = 0,
    @SerializedName("subgroup_facultyoid")
    val subgroupFacultyOId : Int = 0,
    @SerializedName("date")
    val date : String = "",
    @SerializedName("dayOfWeek")
    val dayOfWeek : Int = 0,
    @SerializedName("dayOfWeekString")
    val dayOfWeekString : String = "",
    @SerializedName("lessonOid")
    val lessonOId : Int = 0,
    @SerializedName("beginLesson")
    val beginLesson : String = "",
    @SerializedName("endLesson")
    val endLesson : String = "",
    @SerializedName("duration")
    val duration : Int = 0,
    @SerializedName("contentOfLoadOid")
    val contentOfLoadOId : Int = 0,
    @SerializedName("contentOfLoadUid")
    val contentOfLoadUId : Int = 0,
    @SerializedName("kindOfWorkOid")
    val kindOfWorkOId : Int = 0,
    @SerializedName("kindOfWorkUid")
    val kindOfWorkUId : String = "",
    @SerializedName("kindOfWork")
    val kindOfWork : String = "",
    @SerializedName("discipline")
    val discipline : String = "",
    @SerializedName("disciplineOid")
    val disciplineOId : Int = 0,
    @SerializedName("disciplineinplan")
    val disciplineInPlan : Int = 0,
    @SerializedName("disciplinetypeload")
    val disciplineTypeLoad : Int = 0,
    @SerializedName("building")
    val building : String = "",
    @SerializedName("auditorium")
    val auditorium : String = "",
    @SerializedName("auditoriumOid")
    val auditoriumOId : Int = 0,
    @SerializedName("auditoriumAmount")
    val auditoriumAmount : Int = 0,
    @SerializedName("lecturer")
    val lecturer : String = "",
    @SerializedName("lecturerOid")
    val lecturerOId : Int = 0,
    @SerializedName("lecturerUID")
    val lecturerUId : String = "",
    @SerializedName("lecturerCustomUID")
    val lecturerCustomUId : String = "",
    @SerializedName("author")
    val author : String = "",
    @SerializedName("note")
    val note : String = "",
    @SerializedName("createddate")
    val createdDate : String = "", //хз просто какая то дата, которая возвращается в формате date, не ебу как иначе сохранить
    @SerializedName("dateOfNest")
    val dateOfNest : String = "",
    @SerializedName("hideinCapacity")
    val hideInCapacity : Int = 0, // 1 - скрыть 0 - не скрыть
    @SerializedName("isBan")
    val isBan : Int = 0,
    @SerializedName("lessonNumberStart")
    val lessonNumberStart : Int = 0,
    @SerializedName("lessonNumberEnd")
    val lessonNumberEnd : Int = 0,
    @SerializedName("modifieddate")
    val modifiedDate : String = "", //тоже непонятная дата
    @SerializedName("parentschedule")
    val parentSchedule : String = "",
    @SerializedName("detailinfo")
    val detailInfo : String = "",
    @SerializedName("url1")
    val url1 : String= "",
    @SerializedName("url1_description")
    val url1Description : String = "",
    @SerializedName("url2")
    val url2 : String = "",
    @SerializedName("url2_description")
    val url2Description : String = ""
) : Serializable