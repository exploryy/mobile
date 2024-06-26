package com.example.explory.presentation.screen.auth.login

data class LoginState(
    val login: String = "",
    val password: String = "",
    val isPasswordHide: Boolean = true,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val navigationEvent: LoginNavigationEvent? = null
)

sealed class LoginNavigationEvent {
    data object NavigateToMap : LoginNavigationEvent()
    data object NavigateToRegistration : LoginNavigationEvent()
}