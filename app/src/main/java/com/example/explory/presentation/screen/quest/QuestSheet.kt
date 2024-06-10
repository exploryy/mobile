package com.example.explory.presentation.screen.quest

import androidx.compose.runtime.Composable
import com.example.explory.data.service.PointDto
import com.example.explory.ui.theme.DarkGray
import com.skydoves.flexible.bottomsheet.material3.FlexibleBottomSheet
import com.skydoves.flexible.core.FlexibleSheetSize
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState

@Composable
fun QuestSheet(
    name: String,
    image: String?,
    point: PointDto,
    description: String,
    difficulty: String,
    transportType: String,
    distance: Long,
    onButtonClicked: () -> Unit
) {
    FlexibleBottomSheet(
        onDismissRequest = { },
        sheetState = rememberFlexibleBottomSheetState(
            flexibleSheetSize = FlexibleSheetSize(
                fullyExpanded = 0.9f,
                intermediatelyExpanded = 0.5f,
                slightlyExpanded = 0.2f,
            ),
            isModal = false,
            skipSlightlyExpanded = false,
            skipHiddenState = true,
        ),
        containerColor = DarkGray,
    ) {
        P2PContent(
            name = name,
            point = point,
            image = image,
            description = description,
            difficulty = difficulty,
            transportType = transportType,
            distance = distance,
            onButtonClicked = onButtonClicked
        )
    }
}