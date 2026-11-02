package com.example.anvux.data.remote

import com.example.anvux.data.remote.dto.UserDto
import retrofit2.http.GET

interface UserApi {

    @GET("api/v1/users/me")
    suspend fun me(): UserDto
}