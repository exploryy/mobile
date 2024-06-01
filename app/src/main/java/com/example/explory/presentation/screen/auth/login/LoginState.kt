package com.example.explory.presentation.screen.auth.login

import com.example.explory.common.Constants

data class LoginState(
    val login: String = Constants.DEFAULT_LOGIN,
    val password: String = Constants.DEFAULT_PASSWORD,
    val isPasswordHide: Boolean = true,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val navigationEvent: LoginNavigationEvent? = null
)

sealed class LoginNavigationEvent {
    data object NavigateToMap : LoginNavigationEvent()
    data object NavigateToRegistration : LoginNavigationEvent()
}