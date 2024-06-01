package com.example.explory.domain.usecase

import com.example.explory.data.repository.AuthRepository

class GetUserTokenUseCase(
    private val authRepository: AuthRepository,
) {
    fun execute(): String? {
        return authRepository.getToken()
    }
}