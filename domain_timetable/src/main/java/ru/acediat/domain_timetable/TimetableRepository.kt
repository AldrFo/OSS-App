package ru.acediat.domain_timetable

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import ru.acediat.domain_timetable.dto.GroupDTO
import ru.acediat.domain_timetable.dto.GroupValidDTO
import ru.acediat.domain_timetable.dto.LessonsDTO

class TimetableRepository(private val api: TimetableApi) {

    @SuppressLint("CheckResult")
    fun getWeekTimetable(group : String) : Observable<List<LessonsDTO>> =
        api.getPersonLessons(group, DatePicker.getMondayRUZDate(), DatePicker.getSundayRUZDate())
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())

    @SuppressLint("CheckResult")
    fun getNextWeekTimetable(group : String) : Observable<List<LessonsDTO>> =
        api.getPersonLessons(group, DatePicker.getNextMondayRUZDate(), DatePicker.getNextSundayRUZDate())
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())

    fun getUserGroupName(id : Int) : Observable<GroupDTO> =
        api.getUserGroup(id)
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())

    fun isGroupValid(group : String) : Observable<GroupValidDTO> =
        api.isGroupValid(group)
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())

}