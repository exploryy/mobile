package com.example.explory.domain.usecase

import com.example.explory.data.model.auth.Registration
import com.example.explory.data.model.token.TokenResponse

class PostRegistrationUseCase {
    suspend fun invoke(register: Registration): Result<TokenResponse> {
        return Result.success(TokenResponse("dfssdas", "sdfsd"));
    }
}