package com.example.explory.presentation.screen.inventory

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.explory.presentation.screen.common.getTranslateCategoryName
import com.example.explory.presentation.screen.inventory.component.InventoryItemCard
import com.example.explory.presentation.screen.inventory.component.InventoryItemDialog
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun InventoryScreen(
    viewModel: InventoryViewModel = koinViewModel(),
    onDismiss: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.fetchInventory()
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                viewModel.onCleared()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val inventoryState by viewModel.inventoryState.collectAsState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "Инвентарь",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (inventoryState.inventoryList.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Ваш инвентарь пуст",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                val groupedItems = inventoryState.inventoryList.groupBy { it.cosmeticType }

                LazyColumn(
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    groupedItems.forEach { (type, items) ->
                        stickyHeader {
                            Text(
                                text = getTranslateCategoryName(type.name),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(8.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        items.chunked(2).forEach { rowItems ->
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    rowItems.forEach { item ->
                                        InventoryItemCard(
                                            item = item,
                                            onEquipClick = { viewModel.equipItem(item.itemId) },
                                            onUnEquipClick = { viewModel.unEquipItem(item.itemId) },
                                            onSellClick = { viewModel.sellItem(item.itemId) },
                                            onCardClick = { viewModel.openItemDialog(item) },
                                            modifier = Modifier.weight(1f)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                    if (rowItems.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (inventoryState.isOpenDescriptionDialog && inventoryState.selectedItem != null) {
            InventoryItemDialog(
                onDismiss = { viewModel.closeItemDialog() },
                cosmeticItem = inventoryState.selectedItem!!,
                onEquipClick = { viewModel.equipItem(it) },
                onUnEquipClick = { viewModel.unEquipItem(it) },
                onSellClick = { viewModel.sellItem(it) }
            )
        }
    }
}
