package com.example.explory.presentation.screen.profile

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.explory.presentation.screen.friendprofile.FriendProfileScreen
import com.example.explory.presentation.screen.friends.FriendsScreen
import com.example.explory.presentation.screen.map.component.Avatar
import com.example.explory.presentation.screen.requests.FriendRequestsScreen
import com.example.explory.presentation.screen.userstatistic.UserStatisticScreen
import com.example.explory.ui.theme.S18_W600
import com.example.explory.ui.theme.S20_W600
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    onLogout: () -> Unit,
    onBackClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.getNotificationCount()
        viewModel.fetchProfile()
    }

    LaunchedEffect(profileState.loggedOut) {
        if (profileState.loggedOut) {
            onLogout()
        }
    }

    ModalBottomSheet(
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
                },
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Avatar(
                    image = profileState.profile?.avatarUrl,
                    border = profileState.profile?.inventoryDto?.avatarFrames,
                    modifier = Modifier.size(100.dp)
                )

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    ProfileButton(
                        onClick = { viewModel.changeOpenEditDialogState() },
                        text = "редактировать",
                        modifier = Modifier,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    ProfileButton(
                        onClick = { viewModel.logout() },
                        text = "выйти",
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                }

            }
            Spacer(modifier = Modifier.height(16.dp))

            profileState.profile?.let {
                Text(
                    text = it.username,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = S20_W600
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            profileState.profile?.let {
                Text(
                    text = it.email,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = S18_W600
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            SemiRoundedButtonsRow(
                onFirstButtonClick = { viewModel.changeCurrentPage(1) },
                onSecondButtonClick = { viewModel.changeCurrentPage(2) },
                onThirdButtonClick = { viewModel.changeCurrentPage(3) },
                selectedButton = profileState.profileScreenState,
                notificationCount = profileState.notificationCount
            )

            when (profileState.profileScreenState) {
                1 -> FriendsScreen(
                    onFriendProfileClick = { viewModel.onFriendMarkerClicked(friendId = it) }
                )

                2 -> UserStatisticScreen()
                3 -> FriendRequestsScreen()
            }
        }
    }

    if (profileState.isEditDialogOpen) {
        EditProfileDialog(
            profile = profileState.profile,
            onDismiss = { viewModel.changeOpenEditDialogState() },
            onSave = { viewModel.editProfile(it) }
        )
    }

    if (profileState.showFriendProfileScreen && profileState.selectedFriendId != null) {
        FriendProfileScreen(
            friendId = profileState.selectedFriendId!!,
            onBackClick = { viewModel.closeFriendProfileScreen() }
        )
    }
}

