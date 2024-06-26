package com.example.explory.presentation.screen.completedquests

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import com.example.explory.data.model.quest.CompletedQuestDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompletedQuestScreen(
    quests: List<CompletedQuestDto>,
    onQuestClick: (CompletedQuestDto) -> Unit,
    onDismiss: () -> Unit
){
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)


}