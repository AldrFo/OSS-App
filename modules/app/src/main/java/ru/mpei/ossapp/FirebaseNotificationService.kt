package ru.mpei.ossapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.mpei.service_notifications.R
import java.util.*
import kotlin.random.Random

class FirebaseNotificationService : FirebaseMessagingService() {

    private val SENDER_LOG = "OSS_TAG_SENDER"

    @RequiresApi(Build.VERSION_CODES.O)
    private val channel = NotificationChannel(
        "oss channel", "oss",
        NotificationManager.IMPORTANCE_DEFAULT
    )

    override fun onNewToken(token: String) {
        Log.d(SENDER_LOG, "Refreshed token: $token")

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(SENDER_LOG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            Log.d(SENDER_LOG, task.result)
        })
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(SENDER_LOG, "FROM: ${remoteMessage.from}")

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(SENDER_LOG, "Message data payload: " + remoteMessage.data)
            sendNotification(remoteMessage.data["title"] ?: "error",remoteMessage.data["description"]?:"error")
        }
    }


    private fun sendNotification(title : String, body : String){
        val manager = NotificationManagerCompat.from(baseContext)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(channel)
        }
        val builder = NotificationCompat.Builder(baseContext, "oss channel")
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_notify)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        manager.notify(Random(Date().time).nextInt(), builder.build())
    }
}