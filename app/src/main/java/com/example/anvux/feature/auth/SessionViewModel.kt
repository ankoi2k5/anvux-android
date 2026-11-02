package com.example.anvux.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anvux.data.repository.AuthRepository
import com.example.anvux.data.repository.ProjectRepository
import com.example.anvux.data.repository.TaskRepository
import com.example.anvux.domain.model.SessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository,
) : ViewModel() {

    val sessionState: StateFlow<SessionState> = authRepository.sessionState
        .stateIn(viewModelScope, SharingStarted.Eagerly, SessionState.Loading)

    val userName: StateFlow<String> = authRepository.displayName
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "bạn")

    fun logout() {
        viewModelScope.launch { authRepository.logout() }
    }

    /** Làm mới hồ sơ rồi tải dự án và công việc từ server vào cơ sở dữ liệu cục bộ. */
    fun refreshAll() {
        viewModelScope.launch {
            authRepository.refreshProfile()
            projectRepository.refresh()
            taskRepository.refresh()
        }
    }
}