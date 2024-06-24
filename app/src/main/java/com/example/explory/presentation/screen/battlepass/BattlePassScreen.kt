package com.example.explory.presentation.screen.battlepass

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CropFree
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.explory.data.model.battlepass.BattlePassDto
import com.example.explory.data.model.battlepass.BattlePassLevelDto
import com.example.explory.data.model.battlepass.BattlePassRewardDto
import com.example.explory.data.model.shop.RarityType
import com.example.explory.presentation.screen.common.RoundedSquareAvatar
import com.example.explory.presentation.screen.shop.component.getRarityBrush
import com.example.explory.ui.theme.BattlePass
import com.example.explory.ui.theme.Green
import com.example.explory.ui.theme.S20_W600
import com.example.explory.ui.theme.S24_W600
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BattlePassScreen(
    battlePassViewModel: BattlePassViewModel = koinViewModel(),
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val battlePassState by battlePassViewModel.battlePassState.collectAsState()

    rememberLazyListState()

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
                .verticalScroll(rememberScrollState())
        ) {
            battlePassState.battlePass?.let {
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = it.name.replaceFirstChar { char -> char.lowercaseChar() },
                        style = S24_W600
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CountdownTimer(targetDateTime = it.endDate)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            BattlePassContent(
                items = battlePassState.battlePass?.levels ?: emptyList(),
                currentLevel = battlePassState.battlePass?.currentLevel ?: 0,
                currentExp = battlePassState.userStatisticDto?.experience ?: 0
            )
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
                "%02d дней %02d:%02d:%02d", days, hours, minutes, seconds
            ),
            style = S20_W600,
            color = BattlePass
        )
    }
}


@Composable
fun BattlePassContent(items: List<BattlePassLevelDto>, currentLevel: Int, currentExp: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .border(10.dp, MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))

    ) {
//            items(items.size) { index ->
        items.forEachIndexed { index, _ ->
            BattlePassItem(
                items[index],
                currentLevel,
                currentExp,
                if (index == items.size - 1) -1 else items[index + 1].experienceNeeded
            )
            HorizontalDivider(
                modifier = Modifier.clip(RoundedCornerShape(8.dp)),
                thickness = 10.dp,
                color = MaterialTheme.colorScheme.secondary
            )
        }

    }
}

@Composable
fun BattlePassItem(
    quest: BattlePassLevelDto,
    currentLevel: Int,
    currentExp: Int,
    nextLevelExp: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = quest.level.toString(),
                style = S20_W600,
                modifier = Modifier
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        val progress = when {
            nextLevelExp == -1 -> 0f
            quest.experienceNeeded < currentExp -> 100f
            quest.level + 1 == currentLevel -> {
                min(
                    ((quest.experienceNeeded - currentExp) / (nextLevelExp - quest.experienceNeeded)).toFloat(),
                    100f
                )
            }

            else -> 0f
        }
        Log.d("BattlePassItem", "level = ${quest.level}, progress = $progress")
//        if (quest.level - 1 == currentLevel) {
        CustomBar(progress = progress, backgroundColor = MaterialTheme.colorScheme.secondary)
//        }
        Spacer(modifier = Modifier.weight(1f))
        if (quest.rewards.isNotEmpty()) {
            BattlePassRewardItem(item = quest.rewards.first(), modifier = Modifier.size(80.dp))
        } else {
            BattlePassRewardItem(item = null, modifier = Modifier.size(80.dp))
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun BattlePassRewardItem(modifier: Modifier = Modifier, item: BattlePassRewardDto?) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
            .border(
                3.dp,
                if (item == null) getRarityBrush(rarityType = RarityType.COMMON) else
                    getRarityBrush(rarityType = item.item.rarityType),
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (item == null) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.CropFree,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        } else {
            RoundedSquareAvatar(
                image = item.item.url,
                modifier = Modifier
                    .size(80.dp)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun CustomBar(progress: Float, backgroundColor: Color) {
    Canvas(
        modifier = Modifier
            .size(10.dp, 125.dp)
            .rotate(if (progress == 0f || progress == 100f) 0f else 180f)
//            .clipToBounds()
    ) {
        drawRect(
            color = Green,
            size = Size(10.dp.toPx(), (progress * 125).dp.toPx()),
            topLeft = Offset(0.dp.toPx(), ((1 - progress) * 125).dp.toPx())
        )
        drawRect(
            color = backgroundColor,
            size = Size(10.dp.toPx(), 125.dp.toPx()),
            topLeft = Offset(0.dp.toPx(), 0.dp.toPx())
        )
    }
}