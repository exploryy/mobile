package com.example.explory.data.repository

import android.net.Uri
import com.example.explory.data.model.quest.DistanceQuestDto
import com.example.explory.data.model.quest.PointToPointQuestDto
import com.example.explory.data.model.quest.QuestListDto
import com.example.explory.data.model.quest.TransportType
import com.example.explory.data.service.ApiService
import com.example.explory.data.model.review.SendReviewRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class QuestRepository(
    private val apiService: ApiService,
    private val context: android.content.Context
) {
    suspend fun getQuests(): QuestListDto {
        return apiService.getQuests()
    }

    suspend fun getP2PQuest(id: String): PointToPointQuestDto {
        return apiService.getPointToPointQuest(id)
    }

    suspend fun getDistanceQuest(id: String): DistanceQuestDto {
        return apiService.getDistanceQuest(id)
    }

    suspend fun startQuest(id: String, transportType: TransportType) {
        apiService.startQuest(id, transportType)
    }

    suspend fun cancelQuest(questId: String) {
        apiService.cancelQuest(questId)
    }

    suspend fun sendReview(request: SendReviewRequest) {
        val tempFiles = mutableListOf<File>()
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
        requestBody.addFormDataPart("score", request.rating.toString())
        request.reviewText?.let { requestBody.addFormDataPart("message", it) }
        request.images.forEach {
            val file = it.createTempFile()
            requestBody.addFormDataPart(
                "images",
                file.name,
                file.asRequestBody("image/*".toMediaTypeOrNull())
            )
            tempFiles.add(file)
        }
        apiService.sendReview(
            request.eventId.toString(), requestBody.build()
        )
        for (file in tempFiles) {
            file.delete()
        }
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
