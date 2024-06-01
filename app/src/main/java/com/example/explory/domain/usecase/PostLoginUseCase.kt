package com.example.explory.domain.usecase

import com.example.explory.data.model.AuthRequest
import com.example.explory.data.repository.AuthRepository

class PostLoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun execute(authRequest: AuthRequest) {
        authRepository.login(authRequest.login, authRequest.password)
    }
}