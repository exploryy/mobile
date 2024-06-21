package com.example.explory.domain.usecase

import com.example.explory.data.model.note.NoteMultipart
import com.example.explory.data.repository.NoteRepository

class CreateNoteUseCase(private val repository: NoteRepository) {

    suspend fun execute(note: NoteMultipart) {
        repository.createNote(note)
    }
}