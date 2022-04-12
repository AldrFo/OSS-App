package ru.acediat.feature_timetable.models

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import kekmech.ru.common_kotlin.OSS_TAG
import kekmech.ru.common_navigation.AddScreenForward
import ru.acediat.domain_timetable.LessonsCreator
import ru.acediat.domain_timetable.TimetableRepository
import ru.acediat.domain_timetable.entities.Week
import ru.acediat.domain_timetable.items.LessonItem
import ru.acediat.feature_timetable.SettingsFragment

class TimetableViewModel(
    private val activity : FragmentActivity?,
    private val repository : TimetableRepository,
    private val preferences: SharedPreferences
) {

    val week = MutableLiveData<Week>()
    val group = MutableLiveData<String>()

    var error : String? = null

    private val BAD_REQUEST = "ERROR HTTP 400 Bad Request"

    @SuppressLint("CheckResult")
    fun getGroup() {
        val preferencesGroup = preferences.getString("timetableGroup", "") ?: ""
        if(preferencesGroup != "") {
            group.postValue(preferencesGroup)
        }else if(preferences.getBoolean("isAuth", false)){
            getGroupFromServer()
        }
    }

    @SuppressLint("CheckResult")
    fun getTimetable(){
        repository.getWeekTimetable(group.value!!)
            .subscribe({
                val creator = LessonsCreator()
                val lessonItems = ArrayList<LessonItem>()
                for(lesson in it)
                    lessonItems.add(creator.createLessonFromDTO(lesson))
                week.postValue(Week(lessonItems))
            }, {
                Log.e(OSS_TAG, "ERROR", it)
                if(it.message == BAD_REQUEST)
                    error = BAD_REQUEST
            })
    }

    fun goToSettings() = activity?.supportFragmentManager
        ?.let{ AddScreenForward { SettingsFragment() }.apply(it) }

    @SuppressLint("CheckResult")
    private fun getGroupFromServer(){
        repository
            .getUserGroupName((preferences.getString("userId", "") ?: "").toInt())
            .subscribe({
                group.postValue(it.group)
            }, {
                Log.e(OSS_TAG, "ERROR", it)
            })
    }

}