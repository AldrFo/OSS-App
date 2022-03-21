package ru.mpei.domain_tasks

import android.content.Context
import io.reactivex.Completable
import io.reactivex.Maybe
import ru.mpei.domain_tasks.dto.TasksItem

class TasksDatabaseRepository(context : Context) {

    private val tasksDAO = NotificationsDatabase.getDatabase(context).getTasksDao()

    fun getAllTasks() : Maybe<List<TasksItem>> = tasksDAO.getAllTasks()

    fun insertTasks(tasks : List<TasksItem>) : Completable = tasksDAO.insertTasks(tasks)
}