package com.example.anvux.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anvux.core.ui.components.AnVuxButton
import com.example.anvux.core.ui.components.AnVuxSecondaryButton
import com.example.anvux.core.ui.theme.AnVuxTheme
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HomeScreen(
    onCreateTask: () -> Unit,
    onOpenAiPlanner: () -> Unit,
    onLogout: () -> Unit,
) {
    val state = remember { fakeHomeState() }
    HomeContent(
        state = state,
        onCreateTask = onCreateTask,
        onOpenAiPlanner = onOpenAiPlanner,
        onLogout = onLogout,
    )
}

@Composable
private fun HomeContent(
    state: HomeUiState,
    onCreateTask: () -> Unit,
    onOpenAiPlanner: () -> Unit,
    onLogout: () -> Unit,
) {
    val greeting = remember { greetingFor(LocalTime.now().hour) }
    val dateLabel = remember {
        LocalDate.now().format(
            DateTimeFormatter.ofPattern("EEEE, dd/MM", Locale.forLanguageTag("vi-VN")),
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Column {
                Text(
                    text = "$greeting, ${state.userName}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = dateLabel,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        item {
            ProgressCard(
                completed = state.completedToday,
                total = state.totalToday,
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                StatTile(
                    label = "Sắp đến hạn",
                    value = state.dueSoonCount,
                    valueColor = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f),
                )
                StatTile(
                    label = "Đang trễ hạn",
                    value = state.overdueCount,
                    valueColor = MaterialTheme.colorScheme.error,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        item { SectionTitle("Việc sắp đến hạn") }

        items(items = state.upcomingTasks, key = { it.id }) { task ->
            TaskRow(task = task)
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                AnVuxButton(
                    text = "Nhờ AI lên kế hoạch",
                    onClick = onOpenAiPlanner,
                )
                AnVuxSecondaryButton(
                    text = "Tạo công việc nhanh",
                    onClick = onCreateTask,
                )
                TextButton(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Đăng xuất (tạm thời)")
                }
            }
        }
    }
}

private fun greetingFor(hour: Int): String = when (hour) {
    in 5..10 -> "Chào buổi sáng"
    in 11..13 -> "Chào buổi trưa"
    in 14..17 -> "Chào buổi chiều"
    else -> "Chào buổi tối"
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1020)
@Composable
private fun HomeContentPreview() {
    AnVuxTheme {
        HomeContent(
            state = fakeHomeState(),
            onCreateTask = {},
            onOpenAiPlanner = {},
            onLogout = {},
        )
    }
}