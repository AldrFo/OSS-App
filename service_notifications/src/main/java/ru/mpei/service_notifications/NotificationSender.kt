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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kekmech.ru.common_kotlin.OSS_URL
import kekmech.ru.common_network.retrofit.buildApi
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.mpei.domain_tasks.TasksRepository
import ru.mpei.domain_tasks.dto.TasksItem
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.O)
class NotificationSender(private val context : Context) {

    private val SENDER_LOG = "sender_log"

    var enabled = false
        private set

    private var tasksRepository = TasksRepository(
        Retrofit.Builder()
            .baseUrl(OSS_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .buildApi()
    )

    private var notificationId = 0
    private val manager = NotificationManagerCompat.from(context)
    @RequiresApi(Build.VERSION_CODES.O)
    private val channel = NotificationChannel("oss channel", "oss", NotificationManager.IMPORTANCE_DEFAULT)

    private var tasksList : ArrayList<TasksItem> = arrayListOf()

    init{
        Log.d(SENDER_LOG, "init notification service...")
        channel.description = "descr"
        manager.createNotificationChannel(channel)
        tasksRepository.observeTasks(
            context.getSharedPreferences("settings", Context.MODE_PRIVATE)
                .getString("userId", "")!!, "available")
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .doOnSuccess {
                tasksList = it as ArrayList<TasksItem>
            }.doOnError {

            }.subscribe()
        logTasks("init tasks: ", tasksList)
    }

    fun startSending(){
        enabled = true
        doWithTasks(
            {gotTasks ->
                logTasks("received tasks:", gotTasks)
                var notifyList = gotTasks.filter{ !tasksList.contains(it) }
                logTasks("notify list: ", notifyList)
                for(t in notifyList){
                    sendNotification(TaskNotification(t).getNotification(context))
                    notifyList = notifyList.filter { it != t }
                    tasksList.add(t)
                }
            },
            {
                Log.d(SENDER_LOG, "error!", it)
                this.startSending()
            },
            {Log.d(SENDER_LOG, "succeed!")})
    }

    @SuppressLint("CheckResult")
    private fun doWithTasks(onNext : (gotTasks : List<TasksItem>) -> Unit,
                            onError : (it : Throwable) -> Unit,
                            onComplete : () -> Unit){
        tasksRepository.observeTasks(
            context.getSharedPreferences("settings", Context.MODE_PRIVATE)
                .getString("userId", "")!!, "available")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .repeatWhen { t -> t.delay(10, TimeUnit.SECONDS) }
            .subscribe({onNext(it)}, {onError(it)}, {onComplete()})
    }

    private fun sendNotification(notification : Notification) {
        Log.d(SENDER_LOG, "Sending notification...")
        manager.notify(++notificationId, notification)
    }

    private fun logTasks(message : String, tasks : List<TasksItem>){
        Log.d(SENDER_LOG, message)
        for(item in tasks)
            Log.d(SENDER_LOG, item.taskName)
    }
}