package com.example.anvux.core.network

import com.example.anvux.data.remote.dto.ApiErrorDto
import com.example.anvux.domain.model.DataResult
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

private val errorGson = Gson()

/** Chạy một lệnh gọi API và đổi mọi lỗi mạng/máy chủ thành DataResult.Failure có thông báo tiếng Việt. */
suspend fun <T> safeApiCall(block: suspend () -> T): DataResult<T> = try {
    DataResult.Success(block())
} catch (e: HttpException) {
    DataResult.Failure(e.toUserMessage())
} catch (e: IOException) {
    DataResult.Failure("Không kết nối được máy chủ. Kiểm tra mạng và xem backend đã chạy chưa.")
}

fun HttpException.toUserMessage(): String {
    val body = response()?.errorBody()?.string()
    val error = runCatching { errorGson.fromJson(body, ApiErrorDto::class.java) }.getOrNull()
    return error?.details?.firstOrNull()?.message
        ?: error?.message
        ?: "Có lỗi xảy ra (mã ${code()})"
}