package com.example.explory.domain.usecase

import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.explory.data.repository.AuthRepository

class GetUserIdUseCase(
    private val authRepository: AuthRepository,
) {
    fun execute(): String {
        val token = authRepository.getToken()
        if (!token.isNullOrEmpty()){
            val jwt: DecodedJWT = JWT.decode(token)
            return jwt.getClaim("sub").asString()
        } else {
            return ""
        }
    }
}