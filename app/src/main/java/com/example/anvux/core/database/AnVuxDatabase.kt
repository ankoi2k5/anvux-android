package com.example.anvux.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.anvux.data.local.ProjectDao
import com.example.anvux.data.local.ProjectEntity
import com.example.anvux.data.local.TaskDao
import com.example.anvux.data.local.TaskEntity

@Database(
    entities = [ProjectEntity::class, TaskEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AnVuxDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun taskDao(): TaskDao
}