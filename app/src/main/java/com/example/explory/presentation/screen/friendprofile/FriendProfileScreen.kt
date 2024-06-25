package com.example.explory.presentation.screen.friendprofile

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.explory.R
import com.example.explory.data.model.inventory.CosmeticItemInInventoryDto
import com.example.explory.data.model.statistic.UserStatisticDto
import com.example.explory.presentation.screen.auth.onboarding.component.PageContent
import com.example.explory.presentation.screen.map.component.Avatar
import com.example.explory.presentation.screen.userstatistic.UserStatisticContent
import com.example.explory.ui.theme.Black
import com.example.explory.ui.theme.S18_W600
import com.example.explory.ui.theme.S20_W600
import com.example.explory.ui.theme.White
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendProfileScreen(
    viewModel: FriendProfileViewModel = koinViewModel(),
    friendId: String,
    onBackClick: () -> Unit,
    isModal: Boolean = false
) {
    val friendProfileDto by viewModel.friendProfile.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadFriendProfile(friendId)
    }

    Log.d("FriendProfileScreen", "friendProfileDto: $friendProfileDto")
    if (isModal) {
        ModalBottomSheet(onDismissRequest = onBackClick) {
            FriendProfileContent(
                imageUrl = friendProfileDto?.profileDto?.avatarUrl ?: "",
                borderUrl = friendProfileDto?.profileDto?.inventoryDto?.avatarFrames,
                username = friendProfileDto?.profileDto?.username,
                email = friendProfileDto?.profileDto?.email,
                isPrivate = !viewModel.checkUserPrivacy(),
                userStatisticDto = UserStatisticDto(
                    level = friendProfileDto?.level ?: 0,
                    experience = friendProfileDto?.experience ?: 0,
                    totalExperienceInLevel = friendProfileDto?.totalExperienceInLevel ?: 0,
                    distance = friendProfileDto?.distance ?: 0
                ),
                onBackClick = onBackClick
            )
        }
    } else {
        BottomSheetScaffold(sheetPeekHeight = 200.dp, sheetContent = {
            FriendProfileContent(
                imageUrl = friendProfileDto?.profileDto?.avatarUrl ?: "",
                borderUrl = friendProfileDto?.profileDto?.inventoryDto?.avatarFrames,
                username = friendProfileDto?.profileDto?.username,
                email = friendProfileDto?.profileDto?.email,
                isPrivate = !viewModel.checkUserPrivacy(),
                userStatisticDto = UserStatisticDto(
                    level = friendProfileDto?.level ?: 0,
                    experience = friendProfileDto?.experience ?: 0,
                    totalExperienceInLevel = friendProfileDto?.totalExperienceInLevel ?: 0,
                    distance = friendProfileDto?.distance ?: 0
                ),
                onBackClick = onBackClick
            )
        }) {
        }
    }
}

@Composable
fun FriendProfileContent(
    imageUrl: String,
    borderUrl: CosmeticItemInInventoryDto?,
    username: String?,
    email: String?,
    isPrivate: Boolean,
    userStatisticDto: UserStatisticDto,
    onBackClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {

                Avatar(
                    image = imageUrl,
                    border = borderUrl,
                    modifier = Modifier
                        .size(100.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                username?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = S20_W600
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                email?.let {
                    Text(
                        text = email.toString(),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = S18_W600
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
                Log.d("FriendProfileContent", "isPrivate: $isPrivate")
                if (isPrivate) {
                    Spacer(modifier = Modifier.height(32.dp))
                    EmptyFriendStatistic()
                } else {
                    UserStatisticContent(
                        userStatisticDto = UserStatisticDto(
                            level = userStatisticDto.level,
                            experience = userStatisticDto.experience,
                            totalExperienceInLevel = userStatisticDto.totalExperienceInLevel,
                            distance = userStatisticDto.distance,
                        )
                    )
                }
            }
        }
        IconButton(colors = IconButtonDefaults.iconButtonColors(
            contentColor = Black, containerColor = White
        ),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .padding(end = 24.dp),

            onClick = {
                onBackClick()
            }) {
            Icon(
                painter = painterResource(id = R.drawable.close),
                contentDescription = null,
                tint = Black,
            )
        }
    }
}

@Composable
fun EmptyFriendStatistic() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        PageContent(
            title = "Статистика скрыта",
            description = "Данный пользователь предпочел скрыть свою статистику",
            animation = R.raw.lock,
            iconSize = 64.dp
        )
    }
}