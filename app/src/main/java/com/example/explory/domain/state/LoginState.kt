package com.example.explory.domain.state

data class LoginState (
    val login: String,
    val password: String,
    val isPasswordHide: Boolean,

    val isError: Boolean,
    val isErrorText: String?,

    val isLoading: Boolean
)