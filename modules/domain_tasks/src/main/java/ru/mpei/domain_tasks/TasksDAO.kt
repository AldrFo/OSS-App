package ru.mpei.domain_tasks

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Maybe
import ru.mpei.domain_tasks.dto.TasksItem

@Dao
interface TasksDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task : TasksItem) : Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTasks(task : List<TasksItem>) : Completable

    @Update
    fun updateTask(task : TasksItem) : Completable

    @Query("select * from tasks where id = :id")
    fun getTask(id : String) : Maybe<List<TasksItem>>

    @Query("select * from tasks")
    fun getAllTasks() : Maybe<List<TasksItem>>

    @Delete
    fun deleteTask(task : TasksItem) : Completable
}