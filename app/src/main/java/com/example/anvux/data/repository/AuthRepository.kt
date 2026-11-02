package com.example.anvux.data.repository

import com.example.anvux.core.database.AnVuxDatabase
import com.example.anvux.core.network.safeApiCall
import com.example.anvux.core.security.TokenStore
import com.example.anvux.data.remote.AuthApi
import com.example.anvux.data.remote.UserApi
import com.example.anvux.data.remote.dto.AuthResponseDto
import com.example.anvux.data.remote.dto.LoginRequestDto
import com.example.anvux.data.remote.dto.RefreshTokenRequestDto
import com.example.anvux.data.remote.dto.RegisterRequestDto
import com.example.anvux.domain.model.AuthResult
import com.example.anvux.domain.model.DataResult
import com.example.anvux.domain.model.SessionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val userApi: UserApi,
    private val tokenStore: TokenStore,
    private val database: AnVuxDatabase,
) {
    val sessionState: Flow<SessionState> = tokenStore.sessionFlow.map { session ->
        if (session == null) SessionState.LoggedOut else SessionState.LoggedIn
    }

    /** Tên gọi ngắn (từ cuối của họ tên) dùng cho lời chào. */
    val displayName: Flow<String> = tokenStore.sessionFlow.map { session ->
        session?.fullName?.trim()?.substringAfterLast(' ')?.takeIf { it.isNotEmpty() } ?: "bạn"
    }

    suspend fun login(email: String, password: String): AuthResult = authCall {
        authApi.login(LoginRequestDto(email.trim(), password))
    }

    suspend fun register(fullName: String, email: String, password: String): AuthResult = authCall {
        authApi.register(RegisterRequestDto(fullName.trim(), email.trim(), password))
    }

    suspend fun logout() {
        val session = tokenStore.current()
        tokenStore.clear()
        clearLocalData()
        if (session != null) {
            // Kết quả không quan trọng: máy này đã đăng xuất, thu hồi trên server chỉ là bước phụ.
            safeApiCall { authApi.logout(RefreshTokenRequestDto(session.refreshToken)) }
        }
    }

    /** Hỏi server hồ sơ hiện tại. Đồng thời xác nhận phiên còn hợp lệ (có thể kích hoạt refresh). */
    suspend fun refreshProfile() {
        val result = safeApiCall { userApi.me() }
        if (result is DataResult.Success) {
            tokenStore.updateUser(result.value.id, result.value.email, result.value.fullName)
        }
    }

    private suspend fun authCall(block: suspend () -> AuthResponseDto): AuthResult =
        when (val result = safeApiCall(block)) {
            is DataResult.Success -> {
                val response = result.value
                // Xóa dữ liệu của phiên trước (nếu có) trước khi vào phiên mới.
                clearLocalData()
                tokenStore.saveSession(
                    accessToken = response.accessToken,
                    refreshToken = response.refreshToken,
                    userId = response.user.id,
                    email = response.user.email,
                    fullName = response.user.fullName,
                )
                AuthResult.Success
            }
            is DataResult.Failure -> AuthResult.Failure(result.message)
        }

    private suspend fun clearLocalData() {
        withContext(Dispatchers.IO) { database.clearAllTables() }
    }
}