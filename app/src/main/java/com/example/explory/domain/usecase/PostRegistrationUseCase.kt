package com.example.explory.domain.usecase

import com.example.explory.data.model.Registation
import com.example.explory.data.model.TokenResponse

class PostRegistrationUseCase {
    suspend fun invoke(register: Registation): Result<TokenResponse> {
        return Result.success(TokenResponse("dfssdas", "sdfsd"));
    }
}