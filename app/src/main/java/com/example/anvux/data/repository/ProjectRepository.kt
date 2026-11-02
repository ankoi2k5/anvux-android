package com.example.anvux.data.repository

import com.example.anvux.core.network.safeApiCall
import com.example.anvux.data.local.ProjectDao
import com.example.anvux.data.local.TaskDao
import com.example.anvux.data.mapper.toDomain
import com.example.anvux.data.mapper.toEntity
import com.example.anvux.data.remote.ProjectApi
import com.example.anvux.data.remote.dto.ProjectCreateRequestDto
import com.example.anvux.data.remote.dto.ProjectUpdateRequestDto
import com.example.anvux.domain.model.DataResult
import com.example.anvux.domain.model.Project
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepository @Inject constructor(
    private val api: ProjectApi,
    private val projectDao: ProjectDao,
    private val taskDao: TaskDao,
) {
    /** Giao diện luôn đọc từ cơ sở dữ liệu cục bộ, không đọc thẳng từ mạng. */
    fun observeProjects(): Flow<List<Project>> =
        projectDao.observeActive().map { list -> list.map { it.toDomain() } }

    fun observeProject(id: String): Flow<Project?> =
        projectDao.observeById(id).map { it?.toDomain() }

    /** Tải danh sách từ server rồi ghi đè vào cơ sở dữ liệu cục bộ. */
    suspend fun refresh(): DataResult<Unit> =
        when (val result = safeApiCall { api.list(includeArchived = true) }) {
            is DataResult.Success -> {
                projectDao.replaceAll(result.value.map { it.toEntity() })
                DataResult.Success(Unit)
            }
            is DataResult.Failure -> result
        }

    suspend fun create(name: String, description: String?, color: String?): DataResult<Project> {
        val request = ProjectCreateRequestDto(
            name = name.trim(),
            description = description?.trim()?.ifBlank { null },
            color = color,
        )
        // Khóa mới cho mỗi lần người dùng bấm tạo; OkHttp tự gửi lại cùng khóa nếu mạng chập chờn.
        return when (val result = safeApiCall { api.create(UUID.randomUUID().toString(), request) }) {
            is DataResult.Success -> {
                val entity = result.value.toEntity()
                projectDao.upsert(entity)
                DataResult.Success(entity.toDomain())
            }
            is DataResult.Failure -> result
        }
    }

    suspend fun update(
        id: String,
        name: String? = null,
        description: String? = null,
        color: String? = null,
        archived: Boolean? = null,
    ): DataResult<Project> {
        val request = ProjectUpdateRequestDto(name, description, color, archived)
        return when (val result = safeApiCall { api.update(id, request) }) {
            is DataResult.Success -> {
                val entity = result.value.toEntity()
                projectDao.upsert(entity)
                DataResult.Success(entity.toDomain())
            }
            is DataResult.Failure -> result
        }
    }

    suspend fun delete(id: String): DataResult<Unit> =
        when (val result = safeApiCall { api.delete(id) }) {
            is DataResult.Success -> {
                projectDao.deleteById(id)
                taskDao.deleteByProject(id)
                DataResult.Success(Unit)
            }
            is DataResult.Failure -> result
        }
}