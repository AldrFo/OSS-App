package ru.mpei.domain_tasks

import io.reactivex.Single
import ru.mpei.domain_tasks.dto.TasksItem

class TasksRepository(
    private val tasksApi: TasksApi
){
    fun observeTasks(id: String, type: String): Single<List<TasksItem>> = tasksApi.get(id, type)
}