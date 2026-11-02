package com.example.anvux.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String?,
    val color: String?,
    val archived: Boolean,
    val version: Long,
    val createdAt: Long,
    val updatedAt: Long,
)