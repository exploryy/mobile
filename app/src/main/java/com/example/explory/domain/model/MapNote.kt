package com.example.explory.domain.model

import com.example.explory.data.model.note.NoteDto

data class MapNote(
    val isOpened: Boolean = false,
    val note: NoteDto? = null
)