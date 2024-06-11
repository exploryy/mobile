package com.example.explory.domain.usecase

import com.example.explory.data.model.auth.RegistrationRequest
import com.example.explory.data.repository.RegisterRepository

class PostRegistrationUseCase(
    private val registerRepository: RegisterRepository
) {
    suspend fun invoke(register: RegistrationRequest) {
        registerRepository.register(register)
    }
}