package com.example.explory.data.repository

import com.example.explory.data.model.profile.ProfileDto
import com.example.explory.data.model.profile.ProfileMultipart
import com.example.explory.data.service.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class ProfileRepository(
    private val apiService: ApiService
) {
    suspend fun getProfile() : ProfileDto {
        return apiService.getProfile()
    }

    suspend fun editProfile(profileRequest: ProfileMultipart) : Unit {
        val avatarPart = profileRequest.avatar?.let {
            val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("avatar", it.name, requestFile)
        }
        apiService.updateProfile(
            profileRequest.username,
            profileRequest.email,
            profileRequest.password,
            avatarPart
        )
    }
}