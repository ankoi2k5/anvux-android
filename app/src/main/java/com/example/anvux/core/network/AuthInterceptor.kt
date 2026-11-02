package com.example.anvux.core.network

import com.example.anvux.core.security.TokenStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenStore: TokenStore,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Không gắn token cho đường dẫn đăng nhập/đăng ký/refresh. Backend sẽ từ chối cả
        // yêu cầu công khai nếu token đính kèm đã hết hạn.
        if (request.url.encodedPath.startsWith(AUTH_PATH)) {
            return chain.proceed(request)
        }

        val token = runBlocking { tokenStore.current()?.accessToken }
            ?: return chain.proceed(request)

        return chain.proceed(
            request.newBuilder()
                .header("Authorization", "Bearer $token")
                .build(),
        )
    }

    internal companion object {
        const val AUTH_PATH = "/api/v1/auth/"
    }
}