package com.example.anvux.feature.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.anvux.core.ui.components.AnVuxCard
import com.example.anvux.domain.model.TaskPriority

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground,
    )
}

@Composable
fun ProgressCard(completed: Int, total: Int) {
    val progress = if (total == 0) 0f else completed.toFloat() / total

    AnVuxCard {
        Text(
            text = "Tiến độ hôm nay",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Đã hoàn thành $completed/$total việc",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(12.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Composable
fun StatTile(
    label: String,
    value: Int,
    valueColor: Color,
    modifier: Modifier = Modifier,
) {
    AnVuxCard(modifier = modifier) {
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.headlineMedium,
            color = valueColor,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun TaskRow(task: HomeTask) {
    AnVuxCard(onClick = { }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            androidx.compose.foundation.layout.Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${task.projectName} · ${task.dueLabel}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (task.isOverdue) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            PriorityChip(priority = task.priority)
        }
    }
}

@Composable
private fun PriorityChip(priority: TaskPriority) {
    val color = when (priority) {
        TaskPriority.HIGH -> MaterialTheme.colorScheme.error
        TaskPriority.MEDIUM -> MaterialTheme.colorScheme.secondary
        TaskPriority.LOW -> MaterialTheme.colorScheme.tertiary
    }
    val label = when (priority) {
        TaskPriority.HIGH -> "Cao"
        TaskPriority.MEDIUM -> "Vừa"
        TaskPriority.LOW -> "Thấp"
    }

    Surface(
        shape = MaterialTheme.shapes.small,
        color = color.copy(alpha = 0.16f),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = color,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
        )
    }
}