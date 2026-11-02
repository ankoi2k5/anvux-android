package com.example.anvux.core.network

import com.example.anvux.data.remote.AuthApi
import com.example.anvux.data.remote.UserApi
import com.example.anvux.data.remote.ProjectApi
import com.example.anvux.data.remote.TaskApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private fun baseClientBuilder(): OkHttpClient.Builder {
        // Mức BASIC chỉ ghi phương thức, địa chỉ, mã trạng thái. Không ghi nội dung hay token.
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
            redactHeader("Authorization")
        }
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(logging)
    }

    private fun retrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(ApiConfig.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /** Client trần, không có Authenticator, dùng riêng cho việc refresh để khỏi lặp vô hạn. */
    @Provides
    @Singleton
    @Named("plain")
    fun providePlainClient(): OkHttpClient = baseClientBuilder().build()

    @Provides
    @Singleton
    @Named("refresh")
    fun provideRefreshApi(@Named("plain") client: OkHttpClient): AuthApi =
        retrofit(client).create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        authenticator: TokenAuthenticator,
    ): OkHttpClient = baseClientBuilder()
        .addInterceptor(authInterceptor)
        .authenticator(authenticator)
        .build()

    @Provides
    @Singleton
    fun provideAuthApi(client: OkHttpClient): AuthApi =
        retrofit(client).create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideUserApi(client: OkHttpClient): UserApi =
        retrofit(client).create(UserApi::class.java)
    @Provides
    @Singleton
    fun provideProjectApi(client: OkHttpClient): ProjectApi =
        retrofit(client).create(ProjectApi::class.java)

    @Provides
    @Singleton
    fun provideTaskApi(client: OkHttpClient): TaskApi =
        retrofit(client).create(TaskApi::class.java)
}