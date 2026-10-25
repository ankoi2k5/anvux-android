package com.example.anvux.feature.home

import com.example.anvux.domain.model.TaskPriority

data class HomeTask(
    val id: String,
    val title: String,
    val projectName: String,
    val dueLabel: String,
    val priority: TaskPriority,
    val isOverdue: Boolean = false,
)

data class HomeUiState(
    val userName: String,
    val completedToday: Int,
    val totalToday: Int,
    val dueSoonCount: Int,
    val overdueCount: Int,
    val upcomingTasks: List<HomeTask>,
)