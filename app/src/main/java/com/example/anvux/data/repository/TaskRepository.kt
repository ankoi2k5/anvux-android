package com.example.anvux.data.repository

import com.example.anvux.core.network.safeApiCall
import com.example.anvux.data.local.TaskDao
import com.example.anvux.data.mapper.toDomain
import com.example.anvux.data.mapper.toEntity
import com.example.anvux.data.mapper.toRequest
import com.example.anvux.data.remote.TaskApi
import com.example.anvux.data.remote.dto.TaskCreateRequestDto
import com.example.anvux.domain.model.DataResult
import com.example.anvux.domain.model.Task
import com.example.anvux.domain.model.TaskPatch
import com.example.anvux.domain.model.TaskPriority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val api: TaskApi,
    private val taskDao: TaskDao,
) {
    fun observeTasks(): Flow<List<Task>> =
        taskDao.observeAll().map { list -> list.map { it.toDomain() } }

    fun observeTasksByProject(projectId: String): Flow<List<Task>> =
        taskDao.observeByProject(projectId).map { list -> list.map { it.toDomain() } }

    fun observeTask(id: String): Flow<Task?> =
        taskDao.observeById(id).map { it?.toDomain() }

    suspend fun refresh(): DataResult<Unit> =
        when (val result = safeApiCall { api.list() }) {
            is DataResult.Success -> {
                taskDao.replaceAll(result.value.map { it.toEntity() })
                DataResult.Success(Unit)
            }
            is DataResult.Failure -> result
        }

    suspend fun create(
        projectId: String,
        title: String,
        description: String? = null,
        priority: TaskPriority? = null,
        dueAt: Instant? = null,
        estimatedMinutes: Int? = null,
    ): DataResult<Task> {
        val request = TaskCreateRequestDto(
            projectId = projectId,
            title = title.trim(),
            description = description?.trim()?.ifBlank { null },
            priority = priority?.name,
            dueAt = dueAt?.toString(),
            estimatedMinutes = estimatedMinutes,
        )
        return when (val result = safeApiCall { api.create(UUID.randomUUID().toString(), request) }) {
            is DataResult.Success -> {
                val entity = result.value.toEntity()
                taskDao.upsert(entity)
                DataResult.Success(entity.toDomain())
            }
            is DataResult.Failure -> result
        }
    }

    suspend fun update(id: String, patch: TaskPatch): DataResult<Task> =
        when (val result = safeApiCall { api.update(id, patch.toRequest()) }) {
            is DataResult.Success -> {
                val entity = result.value.toEntity()
                taskDao.upsert(entity)
                DataResult.Success(entity.toDomain())
            }
            is DataResult.Failure -> result
        }

    suspend fun delete(id: String): DataResult<Unit> =
        when (val result = safeApiCall { api.delete(id) }) {
            is DataResult.Success -> {
                taskDao.deleteById(id)
                DataResult.Success(Unit)
            }
            is DataResult.Failure -> result
        }
}