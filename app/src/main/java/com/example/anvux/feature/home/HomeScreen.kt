package com.example.anvux.feature.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.anvux.core.ui.components.AnVuxSecondaryButton
import com.example.anvux.core.ui.components.PlaceholderScreen

@Composable
fun HomeScreen(onLogout: () -> Unit) {
    PlaceholderScreen(
        title = "Trang chủ",
        subtitle = "Dashboard sẽ làm ở Ngày 5",
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        AnVuxSecondaryButton(
            text = "Đăng xuất (tạm thời)",
            onClick = onLogout,
        )
    }
}