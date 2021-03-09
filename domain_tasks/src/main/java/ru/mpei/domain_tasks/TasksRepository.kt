package ru.mpei.domain_tasks

import io.reactivex.Single
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import ru.mpei.domain_tasks.dto.TakeTaskItem
import ru.mpei.domain_tasks.dto.TasksItem

class TasksRepository(
    private val tasksApi: TasksApi
){
    fun observeTasks(id: String, type: String): Single<List<TasksItem>> = tasksApi.get(id, type)
    fun take(task: TakeTaskItem): Single<ResponseBody> = tasksApi.take(task)
}