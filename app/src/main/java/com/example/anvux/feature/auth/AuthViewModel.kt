package com.example.anvux.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anvux.data.repository.AuthRepository
import com.example.anvux.domain.model.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        submit { repository.login(email, password) }
    }

    fun register(fullName: String, email: String, password: String) {
        submit { repository.register(fullName, email, password) }
    }

    fun clearError() {
        _uiState.update { if (it.errorMessage != null) it.copy(errorMessage = null) else it }
    }

    private fun submit(call: suspend () -> AuthResult) {
        if (_uiState.value.isLoading) return
        _uiState.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            _uiState.value = when (val result = call()) {
                AuthResult.Success -> AuthUiState(isSuccess = true)
                is AuthResult.Failure -> AuthUiState(errorMessage = result.message)
            }
        }
    }
}