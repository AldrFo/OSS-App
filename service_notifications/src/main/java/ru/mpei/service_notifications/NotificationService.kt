package ru.mpei.service_notifications

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import kekmech.ru.common_kotlin.OSS_TAG

class NotificationService : Service() {

    private lateinit var sender : NotificationSender

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        val builder = NotificationCompat.Builder(applicationContext, "oss channel")
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_gear_n)
            .setContentTitle("ОСС")
            .setContentText("Мы отправлем вам уведомления :)")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        startForeground(0, builder.build())
        sender = NotificationSender(applicationContext)
        sender.startSending()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroy() {
        super.onDestroy()
        Log.d(OSS_TAG, "Notification service destroyed!")
        sender.stopSending()
    }
}