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
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirm by rememberSaveable { mutableStateOf("") }
    var submitted by rememberSaveable { mutableStateOf(false) }

    val nameError = if (submitted) validateName(name) else null
    val emailError = if (submitted) validateEmail(email) else null
    val passwordError = if (submitted) validatePassword(password) else null
    val confirmError = if (submitted) validateConfirmPassword(password, confirm) else null

    AuthLayout(
        title = "Tạo tài khoản",
        subtitle = "Bắt đầu sắp xếp việc học và công việc cùng AnVux",
    ) {
        AnVuxTextField(
            value = name,
            onValueChange = { name = it },
            label = "Họ và tên",
            errorText = nameError,
        )
        Spacer(modifier = Modifier.height(16.dp))
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
        Spacer(modifier = Modifier.height(16.dp))
        AnVuxTextField(
            value = confirm,
            onValueChange = { confirm = it },
            label = "Nhập lại mật khẩu",
            errorText = confirmError,
            isPassword = true,
        )
        Spacer(modifier = Modifier.height(24.dp))
        AnVuxButton(
            text = "Đăng ký",
            onClick = {
                submitted = true
                val valid = validateName(name) == null &&
                        validateEmail(email) == null &&
                        validatePassword(password) == null &&
                        validateConfirmPassword(password, confirm) == null
                if (valid) onRegisterSuccess()
            },
        )
        Spacer(modifier = Modifier.height(12.dp))
        AnVuxSecondaryButton(
            text = "Đã có tài khoản? Đăng nhập",
            onClick = onNavigateBack,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1020)
@Composable
private fun RegisterScreenPreview() {
    AnVuxTheme {
        RegisterScreen(onRegisterSuccess = {}, onNavigateBack = {})
    }
}