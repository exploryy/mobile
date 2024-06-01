package com.example.explory.domain.state

import com.example.explory.common.Constants

data class LoginState(
    val login: String = Constants.DEFAULT_LOGIN,
    val password: String = Constants.DEFAULT_PASSWORD,
    val isPasswordHide: Boolean = true,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)