package com.example.explory.presentation.screen.userstatistic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextAlign
import com.example.explory.presentation.screen.auth.component.LoadingItem
import com.example.explory.presentation.screen.userstatistic.component.AnimatedCircularProgressIndicator
import com.example.explory.ui.theme.Purple40
import com.example.explory.ui.theme.Purple80
import com.example.explory.ui.theme.PurpleGrey40
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserStatisticScreen(
    viewModel: UserStatisticViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    when {
        state.isLoading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoadingItem()
            }
        }
        state.userStatisticDto != null -> {
            val statistics = state.userStatisticDto

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Моя статистика",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                )

                statistics?.let {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Уровень",
                                    style = MaterialTheme.typography.headlineMedium,
                                )
                                Text(
                                    text = it.level.toString(),
                                    style = MaterialTheme.typography.displayLarge
                                )
                            }
                        }
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Опыт",
                                    style = MaterialTheme.typography.headlineMedium,
                                    textAlign = TextAlign.Center
                                )
                                AnimatedCircularProgressIndicator(
                                    currentValue = it.experience,
                                    maxValue = it.totalExperienceInLevel,
                                    progressBackgroundColor = MaterialTheme.colorScheme.primary,
                                    progressIndicatorColor = MaterialTheme.colorScheme.onSurface,
                                    completedColor = MaterialTheme.colorScheme.primary,
                                    circularIndicatorDiameter = 100.dp
                                )
                            }
                        }
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Дистанция",
                                style = MaterialTheme.typography.headlineMedium,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "${it.distance} m",
                                style = MaterialTheme.typography.displayMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

