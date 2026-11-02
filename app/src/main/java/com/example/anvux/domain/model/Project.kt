package com.example.anvux.domain.model

import java.time.Instant

data class Project(
    val id: String,
    val name: String,
    val description: String?,
    val color: String?,
    val archived: Boolean,
    val version: Long,
    val createdAt: Instant,
    val updatedAt: Instant,
)