package com.example.explory.presentation.screen.auth.register

data class RegistrationState (
    val name: String,
    val email: String,
    val password: String,
    val isSecondScreenAvailable: Boolean,
    val isPasswordHide: Boolean,
    val isError: Boolean,
    val isErrorPasswordText: String?,
    val isErrorNameText: String?,
    val isErrorEmailText: String?,

    val isLoading: Boolean
)