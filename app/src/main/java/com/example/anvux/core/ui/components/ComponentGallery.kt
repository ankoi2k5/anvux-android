package com.example.anvux.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anvux.core.ui.theme.AnVuxTheme

@Composable
fun ComponentGallery() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "AnVux",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = "Bảng component dùng chung",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        AnVuxCard {
            Text(
                text = "Chuẩn bị phỏng vấn thực tập",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Hạn: hôm nay 20:00",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        AnVuxCard(onClick = { }) {
            Text(
                text = "Thẻ bấm được",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
        }

        AnVuxTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            keyboardType = KeyboardType.Email,
        )
        AnVuxTextField(
            value = password,
            onValueChange = { password = it },
            label = "Mật khẩu",
            isPassword = true,
            errorText = if (password.isNotEmpty() && password.length < 8) {
                "Mật khẩu tối thiểu 8 ký tự"
            } else {
                null
            },
        )

        AnVuxButton(
            text = "Đăng nhập",
            onClick = { loading = true },
            isLoading = loading,
        )
        AnVuxSecondaryButton(
            text = "Tắt trạng thái loading",
            onClick = { loading = false },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1020)
@Composable
private fun ComponentGalleryPreview() {
    AnVuxTheme {
        ComponentGallery()
    }
}