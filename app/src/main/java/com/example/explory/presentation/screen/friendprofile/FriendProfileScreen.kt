package com.example.explory.presentation.screen.friendprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.explory.R
import com.skydoves.flexible.bottomsheet.material.FlexibleBottomSheet
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
                val imageUrl = friendProfileDto?.photoUrl
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.DarkGray, shape = CircleShape)
                ) {
                    if (imageUrl != null) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .align(Alignment.Center)
                                .clip(CircleShape)
                        )
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.picture),
                            contentDescription = null,
                            modifier = Modifier
                                .size(64.dp)
                                .align(Alignment.Center),
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                friendProfileDto?.let {
                    Text(
                        text = it.username,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                friendProfileDto?.let {
                    Text(
                        text = it.email,
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
