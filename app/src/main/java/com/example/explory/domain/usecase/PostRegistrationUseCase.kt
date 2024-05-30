package com.example.explory.domain.usecase

import com.example.explory.data.model.Login
import com.example.explory.data.model.TokenResponse

class PostRegistrationUseCase {
    suspend fun invoke(register: Login): Result<TokenResponse> {
        return Result.success(TokenResponse("dfssdas", "sdfsd"));
    }
}