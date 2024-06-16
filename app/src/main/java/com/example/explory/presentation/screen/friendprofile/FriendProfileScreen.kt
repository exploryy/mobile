package com.example.explory.presentation.screen.friendprofile

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.explory.presentation.screen.map.component.Avatar
import com.skydoves.flexible.bottomsheet.material3.FlexibleBottomSheet
import com.skydoves.flexible.core.FlexibleSheetSize
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState
import org.koin.androidx.compose.koinViewModel

@Composable
fun FriendProfileScreen(
    viewModel: FriendProfileViewModel = koinViewModel(),
    friendId: String,
    onBackClick: () -> Unit
) {
    val sheetState = rememberFlexibleBottomSheetState(
        flexibleSheetSize = FlexibleSheetSize(
            fullyExpanded = 0.9f,
            intermediatelyExpanded = 0.6f,
            slightlyExpanded = 0.2f,
        ),
        isModal = false,
        skipSlightlyExpanded = false,
    )
    val focusManager = LocalFocusManager.current
    val friendProfileDto by viewModel.friendProfile.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadFriendProfile(friendId)
    }

    FlexibleBottomSheet(
        onDismissRequest = { onBackClick() },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                val imageUrl = friendProfileDto?.profileDto?.avatarUrl
                val borderUrl = friendProfileDto?.profileDto?.inventoryDto?.avatarFrames

                Avatar(
                    image = imageUrl,
                    border = borderUrl,
                    modifier = Modifier
                        .size(100.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                friendProfileDto?.let {
                    Text(
                        text = it.profileDto.username,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                friendProfileDto?.let {
                    Text(
                        text = it.profileDto.email,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                friendProfileDto?.let {
                    Text(
                        text = "Уровень: ${it.level}",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                friendProfileDto?.let {
                    Text(
                        text = "Опыт: ${it.experience} / ${it.totalExperienceInLevel}",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                friendProfileDto?.let {
                    Text(
                        text = "Пройденное расстояние: ${it.distance} м",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}
