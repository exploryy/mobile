package com.example.explory.data.repository

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.explory.common.Constants
import com.example.explory.data.model.TokenResponse

const val SHARED_PREF = "shared_pref"
const val TOKEN_KEY = "token_key"
const val REFRESH_KEY = "refresh_key"

class LocalStorage(context: Context) {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val sharedPreferences = EncryptedSharedPreferences.create(
        SHARED_PREF,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveToken(token: TokenResponse) {
        sharedPreferences.edit().putString(TOKEN_KEY, token.accessToken).apply()
        sharedPreferences.edit().putString(REFRESH_KEY, token.refreshToken).apply()
    }

    fun getToken(): TokenResponse {
        return TokenResponse(
            sharedPreferences.getString(TOKEN_KEY, Constants.EMPTY_STRING)!!,
            sharedPreferences.getString(REFRESH_KEY, Constants.EMPTY_STRING)!!
        )
    }

    fun isAccessTokenExpired(): Boolean {
        val currentTimeMillis = System.currentTimeMillis()
        val expirationTimeMillis = sharedPreferences.getLong("expiration_time", 0)
        return currentTimeMillis >= expirationTimeMillis
    }

    fun hasToken(): Boolean {
        return sharedPreferences.contains(TOKEN_KEY)
    }

    fun removeToken() {
        sharedPreferences.edit().remove(TOKEN_KEY).apply()
        sharedPreferences.edit().remove(REFRESH_KEY).apply()
    }
}