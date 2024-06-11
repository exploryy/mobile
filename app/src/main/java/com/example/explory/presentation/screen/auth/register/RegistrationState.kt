package com.example.explory.presentation.screen.auth.register

data class RegistrationState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val isPasswordHide: Boolean = false,
    val error: String? = null,
    val isLoading: Boolean = false,
    val navigationEvent: NavigationEvent? = null
)


sealed class NavigationEvent {
    data object NavigateBack : NavigationEvent()
    data object NavigateToMap : NavigationEvent()
}