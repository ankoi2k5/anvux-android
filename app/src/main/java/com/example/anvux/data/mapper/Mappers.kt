package com.example.anvux.data.mapper

import com.example.anvux.data.local.ProjectEntity
import com.example.anvux.data.local.TaskEntity
import com.example.anvux.data.remote.dto.ProjectDto
import com.example.anvux.data.remote.dto.TaskDto
import com.example.anvux.data.remote.dto.TaskUpdateRequestDto
import com.example.anvux.domain.model.Project
import com.example.anvux.domain.model.Task
import com.example.anvux.domain.model.TaskPatch
import com.example.anvux.domain.model.TaskPriority
import com.example.anvux.domain.model.TaskStatus
import java.time.Instant

private fun String.toEpochMillis(): Long = Instant.parse(this).toEpochMilli()

fun ProjectDto.toEntity() = ProjectEntity(
    id = id,
    name = name,
    description = description,
    color = color,
    archived = archived,
    version = version,
    createdAt = createdAt.toEpochMillis(),
    updatedAt = updatedAt.toEpochMillis(),
)

fun ProjectEntity.toDomain() = Project(
    id = id,
    name = name,
    description = description,
    color = color,
    archived = archived,
    version = version,
    createdAt = Instant.ofEpochMilli(createdAt),
    updatedAt = Instant.ofEpochMilli(updatedAt),
)

fun TaskDto.toEntity() = TaskEntity(
    id = id,
    projectId = projectId,
    title = title,
    description = description,
    status = status,
    priority = priority,
    dueAt = dueAt?.toEpochMillis(),
    completedAt = completedAt?.toEpochMillis(),
    estimatedMinutes = estimatedMinutes,
    actualMinutes = actualMinutes,
    version = version,
    createdAt = createdAt.toEpochMillis(),
    updatedAt = updatedAt.toEpochMillis(),
)

fun TaskEntity.toDomain() = Task(
    id = id,
    projectId = projectId,
    title = title,
    description = description,
    status = TaskStatus.entries.firstOrNull { it.name == status } ?: TaskStatus.TODO,
    priority = TaskPriority.entries.firstOrNull { it.name == priority } ?: TaskPriority.MEDIUM,
    dueAt = dueAt?.let(Instant::ofEpochMilli),
    completedAt = completedAt?.let(Instant::ofEpochMilli),
    estimatedMinutes = estimatedMinutes,
    actualMinutes = actualMinutes,
    version = version,
    createdAt = Instant.ofEpochMilli(createdAt),
    updatedAt = Instant.ofEpochMilli(updatedAt),
)

fun TaskPatch.toRequest() = TaskUpdateRequestDto(
    projectId = projectId,
    title = title,
    description = description,
    status = status?.name,
    priority = priority?.name,
    dueAt = dueAt?.toString(),
    clearDueAt = if (clearDueAt) true else null,
    estimatedMinutes = estimatedMinutes,
    actualMinutes = actualMinutes,
)