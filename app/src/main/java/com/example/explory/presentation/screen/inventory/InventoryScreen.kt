package com.example.explory.presentation.screen.inventory

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.explory.data.model.shop.CosmeticType
import com.example.explory.domain.model.ItemFullInfo
import com.example.explory.presentation.screen.auth.component.LoadingItem
import com.example.explory.presentation.screen.common.getTranslateCategoryName
import com.example.explory.presentation.screen.inventory.component.InventoryItemCard
import com.example.explory.presentation.screen.inventory.component.ItemFullInfoDialog
import com.example.explory.ui.theme.S16_W600
import com.example.explory.ui.theme.S24_W600
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
    val listState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        modifier = Modifier.fillMaxSize(),
        sheetState = sheetState
    ) {
        when {
            inventoryState.isLoading -> {
                LoadingItem()
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if (inventoryState.inventoryList.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "ваш инвентарь пуст",
                                style = S16_W600,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        val groupedItems = inventoryState.inventoryList.groupBy { it.cosmeticType }

                        LazyColumn(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(24.dp),
                            state = listState
                        ) {
                            groupedItems.forEach { (type, items) ->
                                stickyHeader {
                                    Text(
                                        text = getTranslateCategoryName(type.name),
                                        style = S24_W600,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        textAlign = TextAlign.Start
                                    )
                                }
                                items.chunked(2).forEach { rowItems ->
                                    item {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        ) {
                                            rowItems.forEach { item ->
                                                InventoryItemCard(
                                                    item = item,
                                                    onEquipClick = {
                                                        viewModel.equipItem(item)
                                                    },
                                                    onUnEquipClick = {
                                                        viewModel.unEquipItem(item)
                                                    },
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
            }
        }


        if (inventoryState.isOpenDescriptionDialog && inventoryState.selectedItem != null) {
            ItemFullInfoDialog(
                onDismiss = { viewModel.closeItemDialog() },
                actionText = "продать",
                onActionClicked = { viewModel.sellItem(it) },
                item = ItemFullInfo(
                    itemId = inventoryState.selectedItem!!.itemId,
                    name = inventoryState.selectedItem!!.name,
                    description = inventoryState.selectedItem!!.description,
                    rarity = inventoryState.selectedItem!!.rarityType,
                    imageUrl = inventoryState.selectedItem!!.url,
                    price = inventoryState.selectedItem!!.price,
                    isSellable = inventoryState.selectedItem!!.isSellable
                )
            )
//            InventoryItemDialog(
//                onDismiss = { viewModel.closeItemDialog() },
//                cosmeticItem = inventoryState.selectedItem!!,
//                onSellClick = { viewModel.sellItem(it) }
//            )
        }
    }
}
