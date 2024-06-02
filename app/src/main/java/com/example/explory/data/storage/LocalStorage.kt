package com.example.explory.data.storage

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.explory.data.model.token.TokenType


const val SHARED_PREF = "shared_pref"
const val ACCESS_KEY = "access_key"
const val REFRESH_KEY = "refresh_key"
const val USER_ACCESS_TOKEN_EXPIRES = "user_access_token_expires"
const val USER_REFRESH_TOKEN_EXPIRES = "user_refresh_token_expires"

class LocalStorage(context: Context) {
    private val masterKey: MasterKey =
        MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        SHARED_PREF,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveToken(
        token: String, expiresIn: Int, tokenType: TokenType
    ) {
        val expiresAt = System.currentTimeMillis() + expiresIn * 1000L

        when (tokenType) {
            TokenType.ACCESS -> {
                sharedPreferences.edit().putString(ACCESS_KEY, token)
                    .putLong(USER_ACCESS_TOKEN_EXPIRES, expiresAt).apply()
            }

            TokenType.REFRESH -> {
                sharedPreferences.edit().putString(REFRESH_KEY, token)
                    .putLong(USER_REFRESH_TOKEN_EXPIRES, expiresAt).apply()
            }
        }
    }


    fun fetchToken(tokenType: TokenType): String? {
        return when (tokenType) {
            TokenType.ACCESS -> sharedPreferences.getString(ACCESS_KEY, null)
            TokenType.REFRESH -> sharedPreferences.getString(REFRESH_KEY, null)
        }
    }

    fun getAccessToken(): String? {
        return fetchToken(TokenType.ACCESS)
    }

    fun isAccessTokenExpired(): Boolean {
        val expiresAt = sharedPreferences.getLong(USER_ACCESS_TOKEN_EXPIRES, 0L)
        return expiresAt < System.currentTimeMillis()
    }

    fun hasToken(): Boolean {
        return sharedPreferences.contains(ACCESS_KEY)
    }

    fun removeTokens() {
        sharedPreferences.edit().remove(ACCESS_KEY).remove(REFRESH_KEY)
            .remove(USER_ACCESS_TOKEN_EXPIRES).remove(USER_ACCESS_TOKEN_EXPIRES).apply()
    }
}