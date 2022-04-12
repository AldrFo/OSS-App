package ru.mpei.service_notifications

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import ru.mpei.domain_tasks.dto.TasksItem

class TaskNotification(private val task: TasksItem){

    fun getNotification(context : Context) : Notification{
        val text = "Появилось задание \"${task.taskName}\""
        val bigText = text + "\n" + task.taskDescription
        val builder = NotificationCompat.Builder(context, "oss channel")
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_notify)
            .setContentTitle("Новое задание!")
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        return builder.build()
    }

}