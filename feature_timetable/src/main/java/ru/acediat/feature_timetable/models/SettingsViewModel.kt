package ru.acediat.feature_timetable.models

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import kekmech.ru.common_kotlin.OSS_TAG
import ru.acediat.domain_timetable.TimetableRepository

class SettingsViewModel(
    private val preferences: SharedPreferences,
    private val repository: TimetableRepository
) {

    var isGroupValid = false
    var wasError = false

    fun loadGroup() : String = preferences.getString("timetableGroup", "") ?: ""

    fun saveGroup(group : String) =
        preferences.edit()
            .putString("timetableGroup", group)
            .commit()

    @SuppressLint("CheckResult")
    fun isGroupValid(context : Context, group : String){
        repository.isGroupValid(group)
            .blockingSubscribe({
                if(it.isGroupValid) {
                    saveGroup(group)
                    isGroupValid = it.isGroupValid
                }else{
                    showToast(context,"Неккоректный ввод группы!")
                }
                wasError = false
            }, {
                showToast(context,"Что-то пошло не так...")
                wasError = true
                Log.e(OSS_TAG, "ERROR", it)
            })
    }

    fun showToast(context : Context, message : String) =
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        ).show()
}