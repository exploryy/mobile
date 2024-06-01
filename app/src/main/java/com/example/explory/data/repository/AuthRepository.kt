package com.example.explory.data.repository

import com.example.explory.data.model.TokenType
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
            response.expires_in.toString(),
            TokenType.ACCESS
        )
        localStorage.saveToken(
            response.refresh_token,
            response.refresh_expires_in.toString(),
            TokenType.REFRESH
        )
    }

    suspend fun refreshToken(refreshToken: String) {
        val response = authService.refreshToken(refreshToken)
        localStorage.saveToken(
            response.access_token,
            response.expires_in.toString(),
            TokenType.ACCESS
        )
        localStorage.saveToken(
            response.refresh_token,
            response.refresh_expires_in.toString(),
            TokenType.REFRESH
        )
    }
}