package com.example.explory.presentation.screen.leaderboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.explory.presentation.screen.leaderboard.component.LeaderButtonRow
import com.example.explory.presentation.screen.leaderboard.component.LeaderItem
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    viewModel: LeaderboardViewModel = koinViewModel(),
    onDismiss: () -> Unit
){
    val leaderboardState by viewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        viewModel.setCurrentScreen(1)
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Рейтинг пользователей",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Мой рейтинг: " + (leaderboardState.leadersList?.userPosition ?: "0"),
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
            )

            Spacer(modifier = Modifier.height(16.dp))

            LeaderButtonRow(
                onFirstButtonClick = { viewModel.setCurrentScreen(1) },
                onSecondButtonClick = { viewModel.setCurrentScreen(2) },
                selectedButton = leaderboardState.currentScreen
            )
        }

        LazyColumn {
            leaderboardState.leadersList?.let {
                items(it.bestUsers) { leader ->
                    LeaderItem(leader)
                }
            }
        }
    }
}