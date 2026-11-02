package com.example.anvux.data.remote.dto

data class RegisterRequestDto(
    val fullName: String,
    val email: String,
    val password: String,
)

data class LoginRequestDto(
    val email: String,
    val password: String,
)

data class RefreshTokenRequestDto(
    val refreshToken: String,
)

data class UserDto(
    val id: String,
    val email: String,
    val fullName: String,
)

data class AuthResponseDto(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresIn: Long,
    val user: UserDto,
)

data class ApiErrorDto(
    val code: String?,
    val message: String?,
    val details: List<ApiErrorDetailDto>?,
)

data class ApiErrorDetailDto(
    val field: String?,
    val message: String?,
)