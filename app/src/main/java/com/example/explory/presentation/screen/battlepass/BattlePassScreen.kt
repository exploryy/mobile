package com.example.explory.presentation.screen.battlepass

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.explory.presentation.screen.battlepass.component.LevelProgressItem
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.example.explory.ui.theme.DarkGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

@SuppressLint("UnrememberedMutableState", "DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BattlePassScreen(
    battlePassViewModel: BattlePassViewModel = koinViewModel(),
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val battlePassState by battlePassViewModel.battlePassState.collectAsState()

    val lineColor = MaterialTheme.colorScheme.primary
    val progressLineColor = DarkGreen

    val listState = rememberLazyListState()
    val totalLevels = battlePassState.battlePass?.levels?.size ?: 1

    val progress = remember { Animatable(0f) }

    val firstVisibleItemIndex = listState.firstVisibleItemIndex
    val firstVisibleItemScrollOffset = listState.firstVisibleItemScrollOffset

    val progressValue = derivedStateOf {
        val totalHeight = (totalLevels - 1).toFloat()
        val currentScroll = firstVisibleItemIndex.toFloat() + (firstVisibleItemScrollOffset.toFloat() / listState.layoutInfo.viewportEndOffset)

        val calculatedProgress = if (totalHeight > 0) {
            ((battlePassState.battlePass?.currentLevel?.toFloat() ?: 0f) / totalHeight - (currentScroll / totalHeight)).coerceIn(0f, 1f)
        } else {
            0f
        }

        calculatedProgress
    }

    LaunchedEffect(progressValue.value) {
        progress.animateTo(
            targetValue = progressValue.value,
            animationSpec = tween(durationMillis = 1000)
        )
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
            battlePassState.battlePass?.let { Text(text = it.name, style = MaterialTheme.typography.headlineLarge) }
            battlePassState.battlePass?.let { Text(text = it.description, style = MaterialTheme.typography.bodyLarge) }
            //Text(text = "Time remaining: $remainingTimeText", style = MaterialTheme.typography.bodyMedium)


            Spacer(modifier = Modifier.height(16.dp))



            Box(modifier = Modifier.fillMaxSize()) {
                Canvas(modifier = Modifier
                    .fillMaxHeight()
                    .width(40.dp)
                    .align(Alignment.CenterStart)) {
                    val strokeWidth = 8.dp.toPx()
                    val centerX = size.width / 2
                    val totalHeight = size.height

                    drawLine(
                        color = lineColor,
                        start = Offset(centerX, 0f),
                        end = Offset(centerX, totalHeight),
                        strokeWidth = strokeWidth
                    )

                    drawLine(
                        color = progressLineColor,
                        start = Offset(centerX, 0f),
                        end = Offset(centerX, totalHeight * progress.value),
                        strokeWidth = strokeWidth
                    )
                }

                LazyColumn(
                    state = listState,
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