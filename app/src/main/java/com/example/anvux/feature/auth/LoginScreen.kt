package com.example.anvux.feature.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.anvux.core.ui.components.AnVuxButton
import com.example.anvux.core.ui.components.AnVuxSecondaryButton
import com.example.anvux.core.ui.components.AnVuxTextField
import com.example.anvux.core.ui.theme.AnVuxTheme

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onLoginSuccess()
    }

    LoginContent(
        uiState = uiState,
        onLogin = viewModel::login,
        onInputChanged = viewModel::clearError,
        onNavigateToRegister = onNavigateToRegister,
    )
}

@Composable
private fun LoginContent(
    uiState: AuthUiState,
    onLogin: (email: String, password: String) -> Unit,
    onInputChanged: () -> Unit,
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
            onValueChange = {
                email = it
                onInputChanged()
            },
            label = "Email",
            errorText = emailError,
            keyboardType = KeyboardType.Email,
        )
        Spacer(modifier = Modifier.height(16.dp))
        AnVuxTextField(
            value = password,
            onValueChange = {
                password = it
                onInputChanged()
            },
            label = "Mật khẩu",
            errorText = passwordError,
            isPassword = true,
        )

        uiState.errorMessage?.let { message ->
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        AnVuxButton(
            text = "Đăng nhập",
            isLoading = uiState.isLoading,
            onClick = {
                submitted = true
                if (validateEmail(email) == null && validatePassword(password) == null) {
                    onLogin(email, password)
                }
            },
        )
        Spacer(modifier = Modifier.height(12.dp))
        AnVuxSecondaryButton(
            text = "Tạo tài khoản mới",
            onClick = onNavigateToRegister,
            enabled = !uiState.isLoading,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1020)
@Composable
private fun LoginScreenPreview() {
    AnVuxTheme {
        LoginContent(
            uiState = AuthUiState(errorMessage = "Email hoặc mật khẩu không đúng"),
            onLogin = { _, _ -> },
            onInputChanged = {},
            onNavigateToRegister = {},
        )
    }
}