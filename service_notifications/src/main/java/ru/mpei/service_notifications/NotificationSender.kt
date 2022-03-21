package ru.mpei.service_notifications

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kekmech.ru.common_kotlin.OSS_URL
import kekmech.ru.common_network.retrofit.buildApi
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.mpei.domain_tasks.AVAILABLE
import ru.mpei.domain_tasks.TasksDatabaseRepository
import ru.mpei.domain_tasks.TasksRepository
import ru.mpei.domain_tasks.dto.TasksItem
import java.util.concurrent.TimeUnit

@SuppressLint("CheckResult")
@RequiresApi(Build.VERSION_CODES.O)
class NotificationSender(private val context : Context) {

    private val SENDER_LOG = "OSS_TAG_SENDER"

    var enabled = false
        private set

    private var notificationId = 0
    private val manager = NotificationManagerCompat.from(context)
    private var localTasks = ArrayList<TasksItem>()

    @RequiresApi(Build.VERSION_CODES.O)
    private val channel = NotificationChannel(
        "oss channel", "oss",
        NotificationManager.IMPORTANCE_DEFAULT
    )

    private val userId = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        .getString("userId", "")!!

    private var tasksRepository = TasksRepository(
        Retrofit.Builder()
            .baseUrl(OSS_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .buildApi()
    )
    private val databaseRepository = TasksDatabaseRepository(context)

    private lateinit var tasksObserver : Disposable

    init{
        Log.d(SENDER_LOG, "init notification sender...")
        channel.description = "oss app notification channel"
        manager.createNotificationChannel(channel)
        getLocalTasks()
    }

    fun startSending(){
        enabled = true
        tasksObserver = repeatWithTasks(10).subscribe({
            Log.d(SENDER_LOG, "Checking...")
            val notifyTasks = it.filter { task -> !localTasks.contains(task) }
            databaseRepository.insertTasks(notifyTasks).subscribe()
            for (task in notifyTasks)
                sendNotification(TaskNotification(task).getNotification(context))
            getLocalTasks()
        }, {
            Log.e(SENDER_LOG, "ERROR!", it)
        })
    }

    fun stopSending(){
        enabled = false
        tasksObserver.dispose()
    }

    private fun repeatWithTasks(repeatTime : Long) : Flowable<List<TasksItem>> =
        getNewTasks().repeatWhen { it.delay(repeatTime, TimeUnit.SECONDS) }

    private fun getNewTasks() : Single<List<TasksItem>> =
        tasksRepository.observeTasks(userId, AVAILABLE)
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.computation())

    private fun getLocalTasks() = databaseRepository.getAllTasks()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe{ localTasks = it as ArrayList<TasksItem> }

    private fun sendNotification(notification : Notification) {
        Log.d(SENDER_LOG, "Sending notification...")
        manager.notify(++notificationId, notification)
    }
}