package ru.acediat.domain_timetable

import android.util.Log
import com.cesarferreira.tempo.*
import kekmech.ru.common_kotlin.OSS_TAG
import java.util.*

class DatePicker {

    companion object{
        private val RUZ_DATE_FORMAT = "yyyy.MM.dd"

        fun currentRUZDate() : String = Tempo.now.toString(RUZ_DATE_FORMAT)

        fun currentDay() : Int = Tempo.now.day

        fun currentDate() : Date = Tempo.now

        fun currentMonth() : Int = Tempo.now.month

        fun getMondayRUZDate() : String = getMondayDate().toString(RUZ_DATE_FORMAT)

        fun getSundayRUZDate() : String = (getMondayDate() + 6.day).toString(RUZ_DATE_FORMAT)

        fun getCurrentWeekDays() : ArrayList<Date> {
            val monday = getMondayDate()
            val weekDays = ArrayList<Date>()
            for(i in 0 until 7)
                weekDays.add(monday + i.day)
            return weekDays
        }

        private fun getMondayDate() : Date {
            var counter = 0
            while(!(Tempo.now - counter.days).isMonday)
                counter++
            return Tempo.now - counter.days
        }
    }

}