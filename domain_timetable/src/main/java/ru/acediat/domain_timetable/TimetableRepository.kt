package ru.acediat.domain_timetable

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.schedulers.Schedulers
import kekmech.ru.common_kotlin.OSS_TAG
import ru.acediat.domain_timetable.entities.Week
import ru.acediat.domain_timetable.items.LessonItem
import kotlin.collections.ArrayList

class TimetableRepository(private val api: TimetableApi) {

    @SuppressLint("CheckResult")
    fun getWeekTimetable(group : String) : Week? {
        var week : Week? = null
        api.getPersonLessons(group, DatePicker.getMondayRUZDate(), DatePicker.getSundayRUZDate())
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .blockingSubscribe({
                val creator = LessonsCreator()
                val lessonItems = ArrayList<LessonItem>()
                for(lesson in it)
                    lessonItems.add(creator.createLessonFromDTO(lesson))
                week = Week(lessonItems)
            }, {
                Log.e(OSS_TAG, "ERROR", it)
            })
        return week
    }

    fun getUserGroupName(id : Int) : String?{
        var groupName : String? = null
        api.getUserGroup(id)
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .blockingSubscribe({
                groupName = it
            }, {
                Log.e(OSS_TAG, "ERROR", it)
            })
        return groupName
    }

}