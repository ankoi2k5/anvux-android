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
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onRegisterSuccess()
    }

    RegisterContent(
        uiState = uiState,
        onRegister = viewModel::register,
        onInputChanged = viewModel::clearError,
        onNavigateBack = onNavigateBack,
    )
}

@Composable
private fun RegisterContent(
    uiState: AuthUiState,
    onRegister: (fullName: String, email: String, password: String) -> Unit,
    onInputChanged: () -> Unit,
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
            onValueChange = {
                name = it
                onInputChanged()
            },
            label = "Họ và tên",
            errorText = nameError,
        )
        Spacer(modifier = Modifier.height(16.dp))
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
        Spacer(modifier = Modifier.height(16.dp))
        AnVuxTextField(
            value = confirm,
            onValueChange = {
                confirm = it
                onInputChanged()
            },
            label = "Nhập lại mật khẩu",
            errorText = confirmError,
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
            text = "Đăng ký",
            isLoading = uiState.isLoading,
            onClick = {
                submitted = true
                val valid = validateName(name) == null &&
                        validateEmail(email) == null &&
                        validatePassword(password) == null &&
                        validateConfirmPassword(password, confirm) == null
                if (valid) onRegister(name, email, password)
            },
        )
        Spacer(modifier = Modifier.height(12.dp))
        AnVuxSecondaryButton(
            text = "Đã có tài khoản? Đăng nhập",
            onClick = onNavigateBack,
            enabled = !uiState.isLoading,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1020)
@Composable
private fun RegisterScreenPreview() {
    AnVuxTheme {
        RegisterContent(
            uiState = AuthUiState(),
            onRegister = { _, _, _ -> },
            onInputChanged = {},
            onNavigateBack = {},
        )
    }
}