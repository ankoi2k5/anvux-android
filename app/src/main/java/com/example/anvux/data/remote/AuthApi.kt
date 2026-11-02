package com.example.anvux.data.remote

import com.example.anvux.data.remote.dto.AuthResponseDto
import com.example.anvux.data.remote.dto.LoginRequestDto
import com.example.anvux.data.remote.dto.RefreshTokenRequestDto
import com.example.anvux.data.remote.dto.RegisterRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/v1/auth/register")
    suspend fun register(@Body body: RegisterRequestDto): AuthResponseDto

    @POST("api/v1/auth/login")
    suspend fun login(@Body body: LoginRequestDto): AuthResponseDto

    @POST("api/v1/auth/refresh")
    suspend fun refresh(@Body body: RefreshTokenRequestDto): AuthResponseDto

    @POST("api/v1/auth/logout")
    suspend fun logout(@Body body: RefreshTokenRequestDto): Response<Unit>
}