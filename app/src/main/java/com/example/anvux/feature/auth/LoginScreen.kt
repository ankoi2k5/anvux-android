package com.example.anvux.feature.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anvux.core.ui.components.AnVuxButton
import com.example.anvux.core.ui.components.AnVuxSecondaryButton
import com.example.anvux.core.ui.components.AnVuxTextField
import com.example.anvux.core.ui.theme.AnVuxTheme

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var submitted by rememberSaveable { mutableStateOf(false) }

    val emailError = if (submitted) validateEmail(email) else null
    val passwordError = if (submitted) validatePassword(password) else null

    AuthLayout(
        title = "AnVux",
        subtitle = "Trợ lý AI đồng hành cùng bạn mỗi ngày",
    ) {
        AnVuxTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            errorText = emailError,
            keyboardType = KeyboardType.Email,
        )
        Spacer(modifier = Modifier.height(16.dp))
        AnVuxTextField(
            value = password,
            onValueChange = { password = it },
            label = "Mật khẩu",
            errorText = passwordError,
            isPassword = true,
        )
        Spacer(modifier = Modifier.height(24.dp))
        AnVuxButton(
            text = "Đăng nhập",
            onClick = {
                submitted = true
                if (validateEmail(email) == null && validatePassword(password) == null) {
                    onLoginSuccess()
                }
            },
        )
        Spacer(modifier = Modifier.height(12.dp))
        AnVuxSecondaryButton(
            text = "Tạo tài khoản mới",
            onClick = onNavigateToRegister,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1020)
@Composable
private fun LoginScreenPreview() {
    AnVuxTheme {
        LoginScreen(onLoginSuccess = {}, onNavigateToRegister = {})
    }
}