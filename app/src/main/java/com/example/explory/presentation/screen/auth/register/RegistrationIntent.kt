package com.example.explory.presentation.screen.auth.register

sealed class RegistrationIntent {
    data class UpdateName(val name: String) : RegistrationIntent()
    data class UpdateEmail(val email: String) : RegistrationIntent()

    data class UpdatePassword(val password: String) : RegistrationIntent()
    data object UpdatePasswordVisibility : RegistrationIntent()
    data class Registration(
        val name: String,
        val email: String,
        val password: String
    ) : RegistrationIntent()

    data object UpdateLoading : RegistrationIntent()
    data object NavigateBack : RegistrationIntent()
    data object NavigateToMap : RegistrationIntent()
}