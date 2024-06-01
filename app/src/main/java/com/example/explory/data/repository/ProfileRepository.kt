package com.example.explory.data.repository

import com.example.explory.data.service.ApiService
import com.example.explory.data.service.ProfileDto

class ProfileRepository(
    private val apiService: ApiService
) {
    suspend fun getProfile() : ProfileDto{
        return apiService.getProfile()
    }
}