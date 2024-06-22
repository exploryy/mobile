package com.example.explory.presentation.screen.friendprofile

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.explory.R
import com.example.explory.presentation.screen.auth.onboarding.component.PageContent
import com.example.explory.presentation.screen.map.component.Avatar
import com.example.explory.presentation.screen.userstatistic.component.AnimatedCircularProgressIndicator
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
                        text = it.profileDto.email.toString(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (!viewModel.checkUserPrivacy()){
                    EmptyFriendStatistic()
                } else {
                    friendProfileDto?.let {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp)
                                    .heightIn(min = 180.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Уровень",
                                        style = MaterialTheme.typography.headlineMedium,
                                    )
                                    Text(
                                        text = it.level.toString(),
                                        fontSize = 85.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp)
                                    .heightIn(min = 180.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Опыт",
                                        style = MaterialTheme.typography.headlineMedium,
                                        textAlign = TextAlign.Center
                                    )
                                    it.experience?.let { it1 ->
                                        it.totalExperienceInLevel?.let { it2 ->
                                            AnimatedCircularProgressIndicator(
                                                currentValue = it1,
                                                maxValue = it2,
                                                progressBackgroundColor = MaterialTheme.colorScheme.primary,
                                                progressIndicatorColor = MaterialTheme.colorScheme.onSurface,
                                                completedColor = MaterialTheme.colorScheme.primary,
                                                circularIndicatorDiameter = 110.dp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Дистанция",
                                    style = MaterialTheme.typography.headlineMedium,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "${it.distance} m",
                                    style = MaterialTheme.typography.displayMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyFriendStatistic(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        PageContent(
            title = "Статистика скрыта",
            description = "Данный пользователь предпочел скрыть свою статистику",
            animation = R.raw.lock
        )
    }
}