package com.example.explory.presentation.screen.userstatistic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.explory.presentation.screen.auth.component.LoadingItem
import com.example.explory.presentation.screen.userstatistic.component.AnimatedCircularProgressIndicator
import com.example.explory.ui.theme.S16_W600
import com.example.explory.ui.theme.S20_W600
import com.example.explory.ui.theme.S24_W600
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.userStatisticDto?.let {
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        StatsBox(
                            modifier = Modifier
                                .weight(1f)
                                .height(180.dp)
                        ) {
                            Text(
                                text = "уровень",
                                style = S20_W600
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = it.level.toString(),
                                style = S24_W600
                            )
                        }
                        StatsBox(
                            modifier = Modifier
                                .weight(1f)
                                .height(180.dp)
                        ) {
//                            Text(
//                                text = "опыт",
//                                style = S20_W600,
//                                textAlign = TextAlign.Center
//                            )
//                            Spacer(modifier = Modifier.height(8.dp))
                            AnimatedCircularProgressIndicator(
                                currentValue = it.experience,
                                maxValue = it.totalExperienceInLevel,
                                progressBackgroundColor = MaterialTheme.colorScheme.onSurface,
                                progressIndicatorColor = MaterialTheme.colorScheme.primary,
                                completedColor = MaterialTheme.colorScheme.primary,
                                circularIndicatorDiameter = 125.dp
                            )
                        }
                    }
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider((-32).dp),
                        tooltip = {
                            Text(
                                text = "${it.distance} метров",
                                style = S16_W600,
                                color = MaterialTheme.colorScheme.primary
                            )
                        },
                        state = rememberTooltipState(),
                    ) {
                        StatsBox(
                            Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        ) {
                            Text(
                                text = "пройдено",
                                style = S20_W600,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = getDistanceString(it.distance),
                                style = S20_W600,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

fun getDistanceString(distance: Int): String {
    return when {
        distance < 1000 -> "$distance метров"
        distance < 1000000 -> "${distance / 1000} км"
        else -> "${distance / 1000000} тыс. км"
    }
}

@Composable
fun StatsBox(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            content()
        }
    }
}