package com.example.explory.data.repository

import android.util.Log
import com.example.explory.data.model.auth.RegistrationRequest
import com.example.explory.data.service.ApiService

class RegisterRepository(
    private val apiService: ApiService,
    private val authRepository: AuthRepository
) {
    suspend fun register(registerRequest: RegistrationRequest) {
        apiService.createUser(
            registerRequest.username,
            registerRequest.email,
            registerRequest.password
        )
        Log.d("RegisterRepository", "User registered")
        val login = authRepository.login(registerRequest.username, registerRequest.password)
        Log.d("RegisterRepository", "User logged in $login")
    }
}