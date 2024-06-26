package com.example.explory.presentation.screen.completedquests

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.explory.data.model.quest.CompletedQuestDto
import com.example.explory.ui.theme.S18_W600
import com.example.explory.ui.theme.S24_W600

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompletedQuestScreen(
    quests: List<CompletedQuestDto>,
    onQuestClick: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        Text(text = "Выполненные квесты", style = S24_W600, modifier = Modifier.padding(16.dp))
        if (quests.isEmpty()){
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "вы еще не выполнили\nни один квест",
                    style = S18_W600,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(quests) { quest ->
                    CompletedQuestCard(
                        quest = quest,
                        onClick = { onQuestClick(quest.questId.toString(), quest.questType) }
                    )
                }
            }
        }
    }
}
