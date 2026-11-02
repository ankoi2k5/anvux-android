package com.example.anvux.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    indices = [Index("projectId"), Index("status"), Index("dueAt")],
)
data class TaskEntity(
    @PrimaryKey val id: String,
    val projectId: String,
    val title: String,
    val description: String?,
    val status: String,
    val priority: String,
    val dueAt: Long?,
    val completedAt: Long?,
    val estimatedMinutes: Int?,
    val actualMinutes: Int?,
    val version: Long,
    val createdAt: Long,
    val updatedAt: Long,
)