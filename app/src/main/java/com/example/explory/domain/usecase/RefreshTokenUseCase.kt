package com.example.explory.domain.usecase

import com.example.explory.data.repository.AuthRepository
import com.example.explory.data.service.AuthTokenResponse

class RefreshTokenUseCase(private val authRepository: AuthRepository) {
    suspend fun execute(refreshToken: String): AuthTokenResponse {
        return authRepository.refreshToken(refreshToken)
    }
}
