package com.example.anvux.data.remote.dto

data class ProjectDto(
    val id: String,
    val name: String,
    val description: String?,
    val color: String?,
    val archived: Boolean,
    val version: Long,
    val createdAt: String,
    val updatedAt: String,
)

data class ProjectCreateRequestDto(
    val name: String,
    val description: String?,
    val color: String?,
)

data class ProjectUpdateRequestDto(
    val name: String? = null,
    val description: String? = null,
    val color: String? = null,
    val archived: Boolean? = null,
)

data class TaskDto(
    val id: String,
    val projectId: String,
    val title: String,
    val description: String?,
    val status: String,
    val priority: String,
    val dueAt: String?,
    val completedAt: String?,
    val estimatedMinutes: Int?,
    val actualMinutes: Int?,
    val version: Long,
    val createdAt: String,
    val updatedAt: String,
)

data class TaskCreateRequestDto(
    val projectId: String,
    val title: String,
    val description: String?,
    val priority: String?,
    val dueAt: String?,
    val estimatedMinutes: Int?,
)

data class TaskUpdateRequestDto(
    val projectId: String? = null,
    val title: String? = null,
    val description: String? = null,
    val status: String? = null,
    val priority: String? = null,
    val dueAt: String? = null,
    val clearDueAt: Boolean? = null,
    val estimatedMinutes: Int? = null,
    val actualMinutes: Int? = null,
)