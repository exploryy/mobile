package com.example.explory.presentation.screen.battlepass

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.explory.presentation.screen.battlepass.component.BattlePassContent
import com.example.explory.presentation.screen.battlepass.component.CountdownTimer
import com.example.explory.ui.theme.S24_W600
import org.koin.androidx.compose.koinViewModel

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


