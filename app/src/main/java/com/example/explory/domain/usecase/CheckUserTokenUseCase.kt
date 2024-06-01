package com.example.explory.domain.usecase

import com.example.explory.data.repository.AuthRepository

class CheckUserTokenUseCase(
    private val authRepository: AuthRepository,
) {
    suspend fun execute(): Boolean {
        return authRepository.hasToken()
    }
}
