package com.example.explory.presentation.screen.auth.register

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.explory.common.Constants
import com.example.explory.domain.usecase.PostRegistrationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.HttpException

class RegistrationViewModel(
    private val context: Context,
    private val postRegistrationUseCase: PostRegistrationUseCase
) : ViewModel() {
    private val emptyState = RegistrationState(
        Constants.EMPTY_STRING,
        Constants.EMPTY_STRING,
        Constants.EMPTY_STRING,
        Constants.FALSE,
        Constants.FALSE,
        Constants.FALSE,
        null,
        null,
        null,
        Constants.FALSE
    )

    private val _state = MutableStateFlow(emptyState)
    val state: StateFlow<RegistrationState> get() = _state


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
//                performRegistration(state.value) {
//                    clearData()
//                }
            }

            RegistrationIntent.UpdateLoading -> {
                _state.value = state.value.copy(
                    isLoading = !_state.value.isLoading
                )
            }

            RegistrationIntent.GoBackToAuth -> {
                //router.toAuth()
            }

            RegistrationIntent.GoBackToFirst -> {
                //router.toRegistration()
            }

            RegistrationIntent.GoToLogin -> {
                //router.toLogin()
            }
        }
    }


    fun isContinueButtonAvailable(): Boolean {
        return state.value.name.isNotEmpty() &&
                state.value.email.isNotEmpty() &&
                state.value.password.isNotEmpty() &&
                state.value.isErrorEmailText == null &&
                state.value.isErrorNameText == null
    }

    private fun clearData() {
        processIntent(RegistrationIntent.UpdateName(Constants.EMPTY_STRING))
        processIntent(RegistrationIntent.UpdateEmail(Constants.EMPTY_STRING))
        processIntent(RegistrationIntent.UpdatePassword(Constants.EMPTY_STRING))
    }

//    private fun performRegistration(registrationState: RegistrationState, afterRegistration: () -> Unit) {
//        val registration = Registration("fdsfd", "dfsd", "fdsffsd")
//        processIntent(RegistrationIntent.UpdateLoading)
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val result = postRegistrationUseCase.invoke(registration)
//                withContext(Dispatchers.Main) {
//                    result.fold(
//                        onSuccess = { tokenResponse ->
//                            LocalStorage(context).saveToken(tokenResponse)
//                            NetworkService.setAuthToken(tokenResponse.accessToken)
//                            afterRegistration()
//                        },
//                        onFailure = { exception ->
//                            handleRegistrationError(exception)
//                        }
//                    )
//                }
//            } catch (e: SocketTimeoutException) {
//                withContext(Dispatchers.Main) {
//                    showToast("Превышено время ожидания соединения. Пожалуйста, проверьте ваше интернет-соединение.")
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    showToast("Произошла ошибка: ${e.message}")
//                }
//            } finally {
//                processIntent(RegistrationIntent.UpdateLoading)
//            }
//        }
//
//    }

    private fun handleRegistrationError(exception: Throwable) {
        when (exception) {
            is HttpException -> when (exception.code()) {
                400 -> showToast("Ошибка регистрации")
                else -> showToast("Неизвестная ошибка: ${exception.code()}")
            }

            else -> showToast("Ошибка соединения с сервером")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}