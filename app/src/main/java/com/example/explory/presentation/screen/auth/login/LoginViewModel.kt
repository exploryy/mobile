package com.example.explory.presentation.screen.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explory.common.Constants
import com.example.explory.data.model.auth.AuthRequest
import com.example.explory.data.websocket.LocationWebSocketClient
import com.example.explory.domain.usecase.PostLoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

class LoginViewModel(
    private val postLoginUseCase: PostLoginUseCase,
    private val webSocketClient: LocationWebSocketClient
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun processIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.Login -> {
                processIntent(LoginIntent.UpdateErrorText(null))
                performLogin(_state.value.login, _state.value.password) {
                    clearData()
                }
            }

            LoginIntent.GoBack -> {
                processIntent(LoginIntent.UpdateErrorText(null))
            }

            is LoginIntent.UpdateLogin -> {
                processIntent(LoginIntent.UpdateErrorText(null))
                _state.value = state.value.copy(login = intent.login.trim())
            }

            is LoginIntent.UpdatePassword -> {
                processIntent(LoginIntent.UpdateErrorText(null))
                _state.value = state.value.copy(password = intent.password.trim())
            }

            is LoginIntent.UpdatePasswordVisibility -> {
                _state.value = state.value.copy(isPasswordHide = !_state.value.isPasswordHide)
            }


            is LoginIntent.UpdateErrorText -> {
                _state.value = state.value.copy(errorMessage = intent.errorText)
            }

            LoginIntent.UpdateLoading -> {
                _state.value = state.value.copy(isLoading = !_state.value.isLoading)
            }

            LoginIntent.GoToRegistration -> {
                _state.value =
                    state.value.copy(navigationEvent = LoginNavigationEvent.NavigateToRegistration)
            }

            LoginIntent.SuccessLogin -> {
                _state.value =
                    state.value.copy(
                        navigationEvent = LoginNavigationEvent.NavigateToMap,
                    )
            }
        }
    }

    fun isLoginButtonAvailable(): Boolean {
        return state.value.password.isNotEmpty() &&
                state.value.login.isNotEmpty()
    }

    private fun clearData() {
        processIntent(LoginIntent.UpdateLogin(Constants.EMPTY_STRING))
        processIntent(LoginIntent.UpdatePassword(Constants.EMPTY_STRING))
    }


    private fun performLogin(username: String, password: String, routeAfterLogin: () -> Unit) {
        val request = AuthRequest(username, password)
        processIntent(LoginIntent.UpdateLoading)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                postLoginUseCase.execute(authRequest = request)
                processIntent(LoginIntent.SuccessLogin)
                webSocketClient.connect()
            } catch (e: SocketTimeoutException) {
                processIntent(LoginIntent.UpdateErrorText("Превышено время ожидания"))
            } catch (e: HttpException) {
                Log.d("LoginViewModel", "Error: ${e.message}")
                processIntent(LoginIntent.UpdateErrorText("Неверный логин или пароль"))
            } catch (e: ConnectException) {
                processIntent(LoginIntent.UpdateErrorText("Не удалось подключиться к серверу"))
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error", e)
                processIntent(LoginIntent.UpdateErrorText("Ошибка сервера"))
            } finally {
                processIntent(LoginIntent.UpdateLoading)
            }
        }
    }
}