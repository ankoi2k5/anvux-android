package com.example.anvux.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Insights
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomTab(
    val route: String,
    val label: String,
    val icon: ImageVector,
)

val bottomTabs = listOf(
    BottomTab(Routes.HOME, "Trang chủ", Icons.Rounded.Home),
    BottomTab(Routes.PROJECTS, "Dự án", Icons.Rounded.Folder),
    BottomTab(Routes.AI_PLANNER, "AI Planner", Icons.Rounded.AutoAwesome),
    BottomTab(Routes.INSIGHTS, "Thống kê", Icons.Rounded.Insights),
)