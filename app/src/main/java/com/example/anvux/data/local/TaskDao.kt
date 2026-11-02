package com.example.anvux.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TaskDao {

    @Query("SELECT * FROM tasks ORDER BY dueAt IS NULL, dueAt ASC, createdAt DESC")
    abstract fun observeAll(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE projectId = :projectId ORDER BY dueAt IS NULL, dueAt ASC, createdAt DESC")
    abstract fun observeByProject(projectId: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    abstract fun observeById(id: String): Flow<TaskEntity?>

    @Upsert
    abstract suspend fun upsert(task: TaskEntity)

    @Upsert
    abstract suspend fun upsertAll(tasks: List<TaskEntity>)

    @Query("DELETE FROM tasks WHERE id = :id")
    abstract suspend fun deleteById(id: String)

    @Query("DELETE FROM tasks WHERE projectId = :projectId")
    abstract suspend fun deleteByProject(projectId: String)

    @Query("DELETE FROM tasks WHERE id NOT IN (:keepIds)")
    abstract suspend fun deleteNotIn(keepIds: List<String>)

    @Transaction
    open suspend fun replaceAll(tasks: List<TaskEntity>) {
        upsertAll(tasks)
        deleteNotIn(tasks.map { it.id })
    }
}