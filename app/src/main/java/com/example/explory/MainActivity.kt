package com.example.explory

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.explory.data.storage.ThemePreferenceManager
import com.example.explory.foreground.LocationService
import com.example.explory.presentation.navigation.AppNavigation
import com.example.explory.ui.theme.ExploryTheme
import com.mapbox.common.MapboxOptions

class MainActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var prefsListener: SharedPreferences.OnSharedPreferenceChangeListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        sharedPreferences = ThemePreferenceManager(this).prefs
        val initialIcons = ThemePreferenceManager(this).getIconsPair()
        val initialIsDarkTheme = ThemePreferenceManager(this).isDarkTheme()
        MapboxOptions.accessToken = BuildConfig.MAPBOX_API_KEY
        setContent {
            var icons by remember { mutableStateOf(initialIcons) }
            var isDarkTheme by remember { mutableStateOf(initialIsDarkTheme) }

            DisposableEffect(Unit) {
                prefsListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                    when (key) {
                        ThemePreferenceManager.KEY_ICON_CURRENT -> {
                            icons = ThemePreferenceManager(this@MainActivity).getIconsPair()
                        }

                        ThemePreferenceManager.KEY_THEME -> {
                            isDarkTheme = ThemePreferenceManager(this@MainActivity).isDarkTheme()
                        }
                    }
                }
                sharedPreferences.registerOnSharedPreferenceChangeListener(prefsListener)

                onDispose {
                    sharedPreferences.unregisterOnSharedPreferenceChangeListener(prefsListener)
                }
            }

            LaunchedEffect(icons) {
                if (icons.first != icons.second)
                    changeEnabledComponent(
                        icons.first,
                        icons.second
                    )
            }

            ExploryTheme(isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Intent(applicationContext, LocationService::class.java).apply {
            action = LocationService.ACTION_STOP
            startService(this)
        }
    }
}

fun Activity.changeEnabledComponent(
    enabled: String,
    disabled: String,
) {
    try {
        packageManager.setComponentEnabledSetting(
            ComponentName(
                this,
                enabled
            ),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        packageManager.setComponentEnabledSetting(
            ComponentName(
                this,
                disabled
            ),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
}