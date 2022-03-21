package ru.mpei.domain_tasks

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.mpei.domain_tasks.dto.TasksItem

@Database(entities = [TasksItem::class], version = 1, exportSchema = true)
abstract class NotificationsDatabase : RoomDatabase(){

    abstract fun getTasksDao() : TasksDAO

    companion object {

        @Volatile
        private var INSTANCE: NotificationsDatabase? = null

        fun getDatabase(context: Context) : NotificationsDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null)
                return tempInstance

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotificationsDatabase::class.java,
                    "notification_database"
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }

}