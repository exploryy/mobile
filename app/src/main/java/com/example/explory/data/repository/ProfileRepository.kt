package com.example.explory.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.explory.data.model.profile.ProfileDto
import com.example.explory.data.model.profile.ProfileMultipart
import com.example.explory.data.model.statistic.UserStatisticDto
import com.example.explory.data.service.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ProfileRepository(
    private val apiService: ApiService,
    private val context: Context
) {
    suspend fun getProfile(): ProfileDto {
        return apiService.getProfile()
    }

    suspend fun editProfile(profileRequest: ProfileMultipart) {
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
        profileRequest.username?.let {
            requestBody.addFormDataPart("username", it)
        }
        profileRequest.email?.let {
            requestBody.addFormDataPart("email", it)
        }
        profileRequest.password?.let {
            if (it.isNotEmpty())
                requestBody.addFormDataPart("password", it)
        }
        profileRequest.avatar?.let {
            Log.d("REPOSITORY22", it.toString())
            val file = it.createTempFile()
            requestBody.addFormDataPart(
                "avatar",
                file.name,
                file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            )
        }



        apiService.updateProfile(
            requestBody.build()
        )
    }

    suspend fun getUserStatistic() : UserStatisticDto {
        return apiService.getUserStatistic()
    }

    private fun Uri.createTempFile(): File {
        val fileStream = context.contentResolver.openInputStream(this)
        val fileBytes = fileStream?.readBytes()
        fileStream?.close()

        val outputDir: File = context.cacheDir
        val outputFile = File.createTempFile("prefix", "-suffix", outputDir)
        if (fileBytes != null) {
            outputFile.writeBytes(fileBytes)
        }
        return outputFile
    }

}