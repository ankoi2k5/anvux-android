package com.example.anvux.data.remote

import com.example.anvux.data.remote.dto.TaskCreateRequestDto
import com.example.anvux.data.remote.dto.TaskDto
import com.example.anvux.data.remote.dto.TaskUpdateRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface TaskApi {

    @GET("api/v1/tasks")
    suspend fun list(): List<TaskDto>

    @POST("api/v1/tasks")
    suspend fun create(
        @Header("Idempotency-Key") idempotencyKey: String,
        @Body body: TaskCreateRequestDto,
    ): TaskDto

    @PATCH("api/v1/tasks/{id}")
    suspend fun update(@Path("id") id: String, @Body body: TaskUpdateRequestDto): TaskDto

    @DELETE("api/v1/tasks/{id}")
    suspend fun delete(@Path("id") id: String)
}