package com.example.anvux.data.remote

import com.example.anvux.data.remote.dto.ProjectCreateRequestDto
import com.example.anvux.data.remote.dto.ProjectDto
import com.example.anvux.data.remote.dto.ProjectUpdateRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProjectApi {

    @GET("api/v1/projects")
    suspend fun list(@Query("includeArchived") includeArchived: Boolean): List<ProjectDto>

    @POST("api/v1/projects")
    suspend fun create(
        @Header("Idempotency-Key") idempotencyKey: String,
        @Body body: ProjectCreateRequestDto,
    ): ProjectDto

    @PATCH("api/v1/projects/{id}")
    suspend fun update(@Path("id") id: String, @Body body: ProjectUpdateRequestDto): ProjectDto

    @DELETE("api/v1/projects/{id}")
    suspend fun delete(@Path("id") id: String)
}