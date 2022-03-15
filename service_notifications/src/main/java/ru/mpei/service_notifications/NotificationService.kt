package ru.mpei.service_notifications

import android.app.Service
import android.content.Intent
import android.os.IBinder

class NotificationService : Service() {

    private lateinit var sender : NotificationSender

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        sender = NotificationSender(applicationContext)
        sender.startSending()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}