package com.example.explory.domain.usecase

import com.example.explory.data.model.note.NoteDto
import com.example.explory.data.repository.NoteRepository

class GetNoteUseCase(private val repository: NoteRepository) {

    suspend fun execute(noteId: Long): NoteDto {
        return repository.getNote(noteId)
    }
}