package com.example.anvux.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProjectDao {

    @Query("SELECT * FROM projects WHERE archived = 0 ORDER BY createdAt DESC")
    abstract fun observeActive(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id")
    abstract fun observeById(id: String): Flow<ProjectEntity?>

    @Upsert
    abstract suspend fun upsert(project: ProjectEntity)

    @Upsert
    abstract suspend fun upsertAll(projects: List<ProjectEntity>)

    @Query("DELETE FROM projects WHERE id = :id")
    abstract suspend fun deleteById(id: String)

    @Query("DELETE FROM projects WHERE id NOT IN (:keepIds)")
    abstract suspend fun deleteNotIn(keepIds: List<String>)

    /** Thay toàn bộ dữ liệu cục bộ bằng danh sách từ server, trong một giao dịch. */
    @Transaction
    open suspend fun replaceAll(projects: List<ProjectEntity>) {
        upsertAll(projects)
        deleteNotIn(projects.map { it.id })
    }
}