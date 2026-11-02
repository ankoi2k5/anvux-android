package com.example.anvux.domain.model

import java.time.Instant

enum class TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE,
}

data class Task(
    val id: String,
    val projectId: String,
    val title: String,
    val description: String?,
    val status: TaskStatus,
    val priority: TaskPriority,
    val dueAt: Instant?,
    val completedAt: Instant?,
    val estimatedMinutes: Int?,
    val actualMinutes: Int?,
    val version: Long,
    val createdAt: Instant,
    val updatedAt: Instant,
)

/** Các thay đổi muốn áp dụng lên một công việc. Trường nào để null thì giữ nguyên. */
data class TaskPatch(
    val projectId: String? = null,
    val title: String? = null,
    val description: String? = null,
    val status: TaskStatus? = null,
    val priority: TaskPriority? = null,
    val dueAt: Instant? = null,
    val clearDueAt: Boolean = false,
    val estimatedMinutes: Int? = null,
    val actualMinutes: Int? = null,
)