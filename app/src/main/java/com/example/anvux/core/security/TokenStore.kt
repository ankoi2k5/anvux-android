package com.example.anvux.core.security

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.sessionDataStore by preferencesDataStore(name = "session")

data class StoredSession(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
    val email: String,
    val fullName: String,
)

@Singleton
class TokenStore @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cipher: TokenCipher,
) {
    private object Keys {
        val ACCESS = stringPreferencesKey("access_token")
        val REFRESH = stringPreferencesKey("refresh_token")
        val USER_ID = stringPreferencesKey("user_id")
        val EMAIL = stringPreferencesKey("email")
        val NAME = stringPreferencesKey("full_name")
    }

    /** null nghĩa là chưa đăng nhập. */
    val sessionFlow: Flow<StoredSession?> = context.sessionDataStore.data
        .map { it.toSession() }
        .distinctUntilChanged()

    suspend fun current(): StoredSession? = sessionFlow.first()

    suspend fun saveSession(
        accessToken: String,
        refreshToken: String,
        userId: String,
        email: String,
        fullName: String,
    ) {
        context.sessionDataStore.edit { prefs ->
            prefs[Keys.ACCESS] = cipher.encrypt(accessToken)
            prefs[Keys.REFRESH] = cipher.encrypt(refreshToken)
            prefs[Keys.USER_ID] = userId
            prefs[Keys.EMAIL] = email
            prefs[Keys.NAME] = fullName
        }
    }

    suspend fun updateUser(userId: String, email: String, fullName: String) {
        context.sessionDataStore.edit { prefs ->
            prefs[Keys.USER_ID] = userId
            prefs[Keys.EMAIL] = email
            prefs[Keys.NAME] = fullName
        }
    }

    suspend fun clear() {
        context.sessionDataStore.edit { it.clear() }
    }

    private fun Preferences.toSession(): StoredSession? {
        val access = this[Keys.ACCESS]?.let(cipher::decrypt) ?: return null
        val refresh = this[Keys.REFRESH]?.let(cipher::decrypt) ?: return null
        return StoredSession(
            accessToken = access,
            refreshToken = refresh,
            userId = this[Keys.USER_ID].orEmpty(),
            email = this[Keys.EMAIL].orEmpty(),
            fullName = this[Keys.NAME].orEmpty(),
        )
    }
}