package com.example.explory.domain.usecase

import com.example.explory.data.model.Registration
import com.example.explory.data.model.TokenResponse

class PostRegistrationUseCase {
    suspend fun invoke(register: Registration): Result<TokenResponse> {
        return Result.success(TokenResponse("dfssdas", "sdfsd"));
    }
}