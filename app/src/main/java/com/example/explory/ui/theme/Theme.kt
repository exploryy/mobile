package com.example.explory.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = AccentColor,
    secondary = DarkGray,
    background = Black,
    surface = Black,
    surfaceVariant = FullBlack,
    onPrimary = White,
    onSecondary = OffWhite,
    onBackground = White,
    onSurface = Gray,
    surfaceContainer = Black,
    onError = Red
)

private val LightColorScheme = lightColorScheme(
    primary = OffWhite,
    secondary = LightGray,
    background = White,
    surfaceVariant = Black,
    surface = OffWhite,
    onPrimary = Black,
    onSecondary = MediumGray,
    onBackground = DarkGray,
    onSurface = OffBlack
)

@Composable
fun ExploryTheme(
    isDarkTheme: Boolean = true, dynamicColor: Boolean = false, content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        isDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
//            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                !isDarkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme, typography = Typography, content = content
    )
}