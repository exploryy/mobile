package com.example.explory.presentation.screen.splash

data class SplashState(
    val navigationEvent: SplashNavigationEvent? = null
)

sealed class SplashNavigationEvent {
    data object NavigateToMap : SplashNavigationEvent()
    data object NavigateToWelcome : SplashNavigationEvent()
}