package com.example.explory.domain.usecase

import com.example.explory.data.model.Login
import com.example.explory.data.model.TokenResponse
import retrofit2.HttpException

class PostLoginUseCase {
    suspend fun invoke(login: Login): Result<TokenResponse> {
        return Result.success(TokenResponse("dfssdas", "sdfsd"));
    }
}