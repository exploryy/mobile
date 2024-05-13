package com.example.explory.presentation.utils

sealed interface UiState {
    data object Default : UiState
    data object Loading : UiState
    data class Error(val message: String) : UiState
    data object Success : UiState
}