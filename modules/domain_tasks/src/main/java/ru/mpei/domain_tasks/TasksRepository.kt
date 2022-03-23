package ru.mpei.domain_tasks

import android.util.Log
import io.reactivex.Single
import kekmech.ru.common_kotlin.OSS_TAG
import okhttp3.ResponseBody
import ru.mpei.domain_tasks.dto.TakeTaskItem
import ru.mpei.domain_tasks.dto.TasksItem

class TasksRepository(
    private val tasksApi: TasksApi
){
    fun observeTasks(id: String, type: String): Single<List<TasksItem>> = tasksApi.get(id, type)

    fun take(task: TakeTaskItem): Single<ResponseBody> =
        tasksApi.take(task.task_id.toInt(), task.user_id.toInt())
}