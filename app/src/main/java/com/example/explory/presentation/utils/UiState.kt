package com.example.explory.presentation.utils

sealed interface UiState {
    data object PermissionGranted : UiState
    data object Default : UiState
    data object Loading : UiState
    data object NoPermissions : UiState
    data class Error(val message: String) : UiState
}