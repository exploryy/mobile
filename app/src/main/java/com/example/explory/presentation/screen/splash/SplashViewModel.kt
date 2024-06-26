package com.example.explory.presentation.screen.splash

import androidx.lifecycle.ViewModel
import com.example.explory.domain.usecase.CheckUserTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SplashViewModel(
    checkUserTokenUseCase: CheckUserTokenUseCase,
) : ViewModel() {
    private val _splashState = MutableStateFlow(SplashState())
    val state = _splashState.asStateFlow()
    init {
        if (checkUserTokenUseCase.execute()) {
            updateNavigationEvent(SplashNavigationEvent.NavigateToMap)
        } else {
            updateNavigationEvent(SplashNavigationEvent.NavigateToWelcome)
        }
    }

    private fun updateNavigationEvent(event: SplashNavigationEvent) {
        _splashState.update { it.copy(navigationEvent = event) }
    }

}

