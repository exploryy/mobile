package com.example.explory.presentation.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.explory.R
import com.example.explory.ui.theme.Black
import com.example.explory.ui.theme.Gray
import com.example.explory.ui.theme.P_S18_W500
import com.example.explory.ui.theme.White
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
            .background(Black), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(224.dp))
            Box(
                modifier = Modifier
                    .background(White, shape = RoundedCornerShape(36.dp))
                    .size(120.dp)
                    .clip(shape = RoundedCornerShape(36.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.compass_alt),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "explore the world", style = P_S18_W500, color = Gray)
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
