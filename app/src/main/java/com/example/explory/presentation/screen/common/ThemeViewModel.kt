package com.example.explory.presentation.screen.common

import androidx.lifecycle.ViewModel
import com.example.explory.data.storage.ThemePreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ThemeViewModel(private val themePreferenceManager: ThemePreferenceManager) : ViewModel() {

    private val _isDarkTheme = MutableStateFlow(themePreferenceManager.isDarkTheme())
    val isDarkTheme: StateFlow<Boolean> get() = _isDarkTheme

    fun setDarkTheme(isDark: Boolean) {
        themePreferenceManager.setDarkTheme(isDark)
        _isDarkTheme.value = isDark
    }
}