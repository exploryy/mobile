package com.example.explory.presentation.screen.auth.login

import com.example.explory.common.Constants

data class LoginState(
    val login: String = Constants.EMPTY_STRING,
    val password: String = Constants.EMPTY_STRING,
    val isPasswordHide: Boolean = false,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val navigationEvent: LoginNavigationEvent? = null
)

sealed class LoginNavigationEvent {
    data object NavigateToMap : LoginNavigationEvent()
    data object NavigateToRegistration : LoginNavigationEvent()
}