package com.example.explory.presentation.screen.userstatistic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.explory.presentation.screen.auth.component.LoadingItem
import com.example.explory.presentation.screen.userstatistic.component.UserStatisticContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserStatisticScreen(
    viewModel: UserStatisticViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchUserStatistics()
    }

    when {
        state.isLoading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                LoadingItem()
            }
        }

        state.userStatisticDto != null -> {
            UserStatisticContent(
                userStatisticDto = state.userStatisticDto!!
            )

        }
    }
}

