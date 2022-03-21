package ru.mpei.service_notifications

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import ru.mpei.domain_tasks.dto.TasksItem

class TaskNotification(private val task: TasksItem){

    fun getNotification(context : Context) : Notification{
        val builder = NotificationCompat.Builder(context, "oss channel")
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_gear_n)
            .setContentTitle("Новое задание!")
            .setContentText("Появилось задание ${task.taskName}, стоимостью ${task.price}!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        return builder.build()
    }

}