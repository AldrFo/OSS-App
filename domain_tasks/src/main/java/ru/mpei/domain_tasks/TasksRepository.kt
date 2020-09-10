package ru.mpei.domain_tasks

import io.reactivex.Single
import ru.mpei.domain_tasks.dto.TasksItem

class TasksRepository(
    private val tasksApi: TasksApi
){
    fun observeTasks(): Single<List<TasksItem>> = tasksApi.get()
}