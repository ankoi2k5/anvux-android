package com.example.anvux.core.network

import com.example.anvux.core.security.TokenStore
import com.example.anvux.data.remote.AuthApi
import com.example.anvux.data.remote.dto.RefreshTokenRequestDto
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * OkHttp gọi lớp này khi server trả 401. Ta thử đổi refresh token lấy token mới, rồi để
 * OkHttp gửi lại đúng yêu cầu ban đầu. Trả về null nghĩa là bỏ cuộc.
 */
@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenStore: TokenStore,
    @Named("refresh") private val refreshApi: Lazy<AuthApi>,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // 401 của chính đường dẫn auth (ví dụ sai mật khẩu) không phải do token hết hạn.
        if (response.request.url.encodedPath.startsWith(AuthInterceptor.AUTH_PATH)) return null
        // Đã thử lại mà vẫn 401 thì dừng, tránh lặp vô hạn.
        if (responseCount(response) >= 2) return null

        return synchronized(this) {
            val session = runBlocking { tokenStore.current() } ?: return@synchronized null
            val usedToken = response.request.header("Authorization")?.removePrefix("Bearer ")

            // Một yêu cầu khác vừa làm mới token trong lúc ta chờ: dùng luôn token mới.
            if (usedToken != null && usedToken != session.accessToken) {
                return@synchronized response.request.newBuilder()
                    .header("Authorization", "Bearer ${session.accessToken}")
                    .build()
            }

            try {
                val refreshed = runBlocking {
                    refreshApi.get().refresh(RefreshTokenRequestDto(session.refreshToken))
                }
                runBlocking {
                    tokenStore.saveSession(
                        accessToken = refreshed.accessToken,
                        refreshToken = refreshed.refreshToken,
                        userId = refreshed.user.id,
                        email = refreshed.user.email,
                        fullName = refreshed.user.fullName,
                    )
                }
                response.request.newBuilder()
                    .header("Authorization", "Bearer ${refreshed.accessToken}")
                    .build()
            } catch (e: HttpException) {
                // Server từ chối refresh token: phiên đã hết, buộc đăng nhập lại.
                if (e.code() == 401) runBlocking { tokenStore.clear() }
                null
            } catch (e: IOException) {
                // Mất mạng: giữ nguyên phiên, thử lại lần sau.
                null
            }
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}