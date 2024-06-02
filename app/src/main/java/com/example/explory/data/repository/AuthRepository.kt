package com.example.explory.data.repository

import android.util.Log
import com.example.explory.data.model.token.AuthTokenResponse
import com.example.explory.data.model.token.TokenType
import com.example.explory.data.service.AuthService
import com.example.explory.data.storage.LocalStorage

class AuthRepository(
    private val authService: AuthService,
    private val localStorage: LocalStorage
) {
    suspend fun login(login: String, password: String) {
        val response = authService.login(login, password)
        localStorage.saveToken(
            response.access_token,
            response.expires_in,
            TokenType.ACCESS
        )
        localStorage.saveToken(
            response.refresh_token,
            response.refresh_expires_in,
            TokenType.REFRESH
        )
    }

    suspend fun refreshToken(refreshToken: String): AuthTokenResponse {
        return authService.refreshToken(refreshToken)
    }

    suspend fun logout() {
        val refreshToken = localStorage.fetchToken(TokenType.REFRESH)
        if (refreshToken != null) {
            Log.d("AuthRepository", "logout: $refreshToken")
            authService.logout(refreshToken)
            Log.d("AuthRepository", "logout: after logout")
        }
        localStorage.removeTokens()
    }

    fun getToken(): String? {
        return localStorage.getAccessToken()
    }

    fun hasToken(): Boolean {
        return localStorage.hasToken()
    }

}