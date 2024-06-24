package com.example.explory.presentation.screen.battlepass

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.explory.data.model.battlepass.BattlePassDto
import com.example.explory.presentation.screen.battlepass.component.LevelProgressItem
import com.example.explory.ui.theme.BattlePass
import com.example.explory.ui.theme.DarkGreen
import com.loukwn.stagestepbar_compose.StageStepBar
import com.loukwn.stagestepbar_compose.data.DrawnComponent
import com.loukwn.stagestepbar_compose.data.Orientation
import com.loukwn.stagestepbar_compose.data.StageStepBarConfig
import com.loukwn.stagestepbar_compose.data.State
import com.loukwn.stagestepbar_compose.data.VerticalDirection
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("UnrememberedMutableState", "DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BattlePassScreen(
    battlePassViewModel: BattlePassViewModel = koinViewModel(),
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val battlePassState by battlePassViewModel.battlePassState.collectAsState()

    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        battlePassViewModel.loadBattlePass()
        battlePassViewModel.loadStatistic()
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            battlePassState.battlePass?.let {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(200.dp)
//                ) {
//                    val backgroundPainter: Painter =
//                        painterResource(id = R.drawable.battlepass_background)
//                    Image(
//                        painter = backgroundPainter,
//                        contentDescription = null,
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .clip(RoundedCornerShape(8.dp)),
//                        contentScale = ContentScale.Crop
//                    )
//                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = it.name,
                        style = TextStyle(
                            color = BattlePass,
                            fontWeight = FontWeight.Bold,
                            fontSize = 36.sp
                        )
                    )
                    CountdownTimer(targetDateTime = it.endDate)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                Row(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(top = 16.dp, start = 16.dp, end = 32.dp)
                    ) {
                        battlePassState.battlePass?.let {
                            getExperienceNeededForNextLevels(
                                it
                            )
                        }?.let {
                            StageStepBarConfig.default()
                                .copy(
                                    stageStepConfig = it,
                                    orientation = Orientation.Vertical,
                                    currentState = battlePassState.userStatisticDto?.let { it1 ->
                                        State(
                                            battlePassState.battlePass?.currentLevel?.minus(
                                                1
                                            ) ?: 1, it1.experience
                                        )
                                    },
                                    verticalDirection = VerticalDirection.Ttb,
                                    animationSpec = tween(durationMillis = 1000),
                                    filledTrack = DrawnComponent.Default(DarkGreen.copy(alpha = 0.8f)),
                                    filledThumb = DrawnComponent.Default(DarkGreen)
                                )
                        }?.let {
                            StageStepBar(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(2.dp),
                                config = it
                            )
                        }
                    }

                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        battlePassState.battlePass?.let {
                            items(it.levels.size) { index ->
                                val level = it.levels[index]
                                LevelProgressItem(level, it.currentLevel)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getExperienceNeededForNextLevels(battlePass: BattlePassDto): List<Int> {
    val experienceNeededList = mutableListOf<Int>()
    for (i in 0 until battlePass.levels.size - 1) {
        val experienceNeeded =
            battlePass.levels[i + 1].experienceNeeded - battlePass.levels[i].experienceNeeded
        experienceNeededList.add(experienceNeeded)
    }
    return experienceNeededList
}

@Composable
fun CountdownTimer(targetDateTime: String) {
    var remainingTime by remember { mutableStateOf<Duration?>(null) }
    val dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME
    val targetTime = LocalDateTime.parse(targetDateTime, dateTimeFormatter)

    LaunchedEffect(Unit) {
        while (true) {
            val currentTime = LocalDateTime.now()
            remainingTime = Duration.between(currentTime, targetTime)
            delay(1000L)
        }
    }

    remainingTime?.let {
        val days = it.toDays()
        val hours = it.toHours() % 24
        val minutes = it.toMinutes() % 60
        val seconds = it.seconds % 60

        Text(
            text = String.format(
                Locale.getDefault(),
                "Осталось: " + "%02d дней %02d:%02d:%02d", days, hours, minutes, seconds
            ),
            style = TextStyle(color = BattlePass, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        )
    }
}

//@Preview
//@Composable
//private fun BattlePassTest() {
//
//    val items = listOf(
//        BattlePassLevel(1, 100, "Reward 1"),
//        BattlePassLevel(2, 200, "Reward 2"),
//        BattlePassLevel(3, 300, "Reward 3"),
//        BattlePassLevel(4, 400, "Reward 4"),
//        BattlePassLevel(5, 500, "Reward 5"),
//        BattlePassLevel(6, 600, "Reward 6"),
//        BattlePassLevel(7, 700, "Reward 7"),
//        BattlePassLevel(8, 800, "Reward 8"),
//        BattlePassLevel(9, 900, "Reward 9"),
//        BattlePassLevel(10, 1000, "Reward 10"),
//    )
//
//    LazyColumn {
//        items(items.size) { index ->
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.padding(8.dp)
//            ) {
//                // Прогресс бар
//                Box(modifier = Modifier
//                    .weight(1f)
//                    .height(24.dp)
//                ) {
//                    LinearProgressIndicator(
//                        progress = items[index].currentExperience / items[index].experienceNeeded.toFloat(),
//                        modifier = Modifier.fillMaxSize()
//                    )
//                    // Текст уровня на прогресс баре
//                    Text(
//                        text = items[index].level.toString(),
//                        modifier = Modifier.align(Alignment.CenterStart)
//                            .padding(start = 8.dp)
//                    )
//                }
//
//                Spacer(modifier = Modifier.width(24.dp))
//
//                // Награда
//                Text(
//                    text = items[index].reward,
//                    modifier = Modifier.align(Alignment.CenterVertically)
//                )
//            }
//        }
//    }
//}
//
//data class BattlePassLevel(
//    val level: Int,
//    val experienceNeeded: Int,
//    val reward: String
//)