package com.example.explory.presentation.screen.auth.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explory.data.model.auth.RegistrationRequest
import com.example.explory.domain.usecase.PostRegistrationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegistrationViewModel(
    private val postRegistrationUseCase: PostRegistrationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegistrationState())
    val state: StateFlow<RegistrationState> get() = _state.asStateFlow()


    fun processIntent(intent: RegistrationIntent) {
        when (intent) {
            is RegistrationIntent.UpdateEmail -> {
                _state.value = state.value.copy(email = intent.email.trim())
            }

            is RegistrationIntent.UpdateName -> {
                _state.value = state.value.copy(name = intent.name)
            }

            is RegistrationIntent.UpdatePassword -> {
                _state.value = state.value.copy(password = intent.password.trim())
            }

            is RegistrationIntent.UpdatePasswordVisibility -> {
                _state.value = state.value.copy(
                    isPasswordHide = !_state.value.isPasswordHide
                )
            }

            is RegistrationIntent.Registration -> {
                performRegistration(
                    intent.email,
                    intent.name,
                    intent.password
                )
            }

            RegistrationIntent.UpdateLoading -> {
                _state.value = state.value.copy(
                    isLoading = !_state.value.isLoading
                )
            }

            RegistrationIntent.NavigateBack -> _state.update {
                it.copy(navigationEvent = NavigationEvent.NavigateBack)
            }

            RegistrationIntent.NavigateToMap -> _state.update {
                it.copy(navigationEvent = NavigationEvent.NavigateToMap)
            }
        }
    }


    fun isContinueButtonAvailable(): Boolean {
        return state.value.name.isNotEmpty() &&
                state.value.email.isNotEmpty() &&
                state.value.password.isNotEmpty()
    }

    private fun performRegistration(
        email: String, username: String, password: String
    ) {
        val registrationRequest = RegistrationRequest(username, email, password)
        processIntent(RegistrationIntent.UpdateLoading)
        viewModelScope.launch {
            try {
                postRegistrationUseCase.invoke(registrationRequest)
                processIntent(RegistrationIntent.NavigateToMap)
            } catch (e: Exception) {
                Log.e("RegistrationViewModel", "Error", e)
                handleRegistrationError(e)
            } finally {
                processIntent(RegistrationIntent.UpdateLoading)
            }
        }

    }

    private fun handleRegistrationError(exception: Throwable) {
        when (exception) {
            is HttpException -> when (exception.code()) {
                400 -> _state.update {
                    it.copy(error = "Пользователь с таким email уже существует")
                }

                else -> _state.update {
                    it.copy(error = "Ошибка сервера")
                }
            }

            else -> _state.update {
                it.copy(error = "Ошибка сервера")
            }
        }
    }
}