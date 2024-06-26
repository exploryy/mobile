package com.example.explory.presentation.screen.friendprofile

import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.explory.data.model.statistic.UserStatisticDto
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

