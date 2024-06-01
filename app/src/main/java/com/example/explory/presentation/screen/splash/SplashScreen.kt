package com.example.explory.presentation.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.explory.R
import com.example.explory.ui.theme.AccentGradient
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = koinViewModel(),
    onNavigateToMap: () -> Unit,
    onNavigateToWelcome: () -> Unit

) {
    val splashState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(splashState.navigationEvent) {
        splashState.navigationEvent?.let { event ->
            when (event) {
                SplashNavigationEvent.NavigateToMap -> onNavigateToMap()
                SplashNavigationEvent.NavigateToWelcome -> onNavigateToWelcome()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = AccentGradient, start = Offset.Infinite, end = Offset.Zero
                )
            ), contentAlignment = Alignment.Center
    ) {
        Image(painter = painterResource(id = R.drawable.compass), contentDescription = null)
    }
}