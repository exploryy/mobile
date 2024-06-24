package com.example.explory.data.storage

import android.content.Context
import android.content.SharedPreferences

class ThemePreferenceManager(context: Context) {
    companion object {
        private const val PREFS_NAME = "prefs"
        const val KEY_THEME = "theme"
        const val KEY_ICON_CURRENT = "current_app_icon"
        private const val KEY_ICON_OLD = "old_app_icon"

    }

    val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isDarkTheme(): Boolean {
        return prefs.getBoolean(KEY_THEME, true)
    }

    fun setDarkTheme(isDark: Boolean) {
        prefs.edit().putBoolean(KEY_THEME, isDark).apply()
    }

    fun setCurrentIcon(aliasName: String) {
        prefs.edit().putString(KEY_ICON_CURRENT, aliasName)
            .putString(KEY_ICON_OLD, getCurrentIcon()).apply()
    }

    private fun getCurrentIcon(): String {
        return prefs.getString(KEY_ICON_CURRENT, "") ?: ""
    }

    private fun getOldIcon(): String {
        return prefs.getString(KEY_ICON_OLD, "") ?: "com.example.explory.MainActivity"
    }

    fun getIconsPair(): Pair<String, String> {
        return Pair(getCurrentIcon(), prefs.getString(KEY_ICON_OLD, "") ?: "")
    }
}