package com.example.explory.domain.usecase

import com.example.explory.data.model.note.CommonNoteDto
import com.example.explory.data.repository.NoteRepository

class GetAllNotesUseCase(private val repository: NoteRepository) {
    suspend fun execute(): List<CommonNoteDto> {
        return repository.getAllNotes()
    }
}