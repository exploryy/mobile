package com.example.explory.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.explory.data.model.note.CommonNoteDto
import com.example.explory.data.model.note.NoteDto
import com.example.explory.data.model.note.NoteMultipart
import com.example.explory.data.service.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class NoteRepository(
    private val api: ApiService,
    private val context: Context
) {
    suspend fun getAllNotes(): List<CommonNoteDto> {
        return api.getAllNotes()
    }

    suspend fun getNote(noteId: Long): NoteDto {
        return api.getNote(noteId)
    }

    suspend fun createNote(note: NoteMultipart) {
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
        requestBody.addFormDataPart("text", note.text)
        requestBody.addFormDataPart("latitude", note.latitude)
        requestBody.addFormDataPart("longitude", note.longitude)
        Log.d("REPOSITORY", note.images.toString())
        note.images.forEachIndexed { index, uri ->
            val file = uri.createTempFile()
            val fileBody = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            requestBody.addFormDataPart("images", file.name, fileBody)
        }

        api.createNote(requestBody.build())
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

