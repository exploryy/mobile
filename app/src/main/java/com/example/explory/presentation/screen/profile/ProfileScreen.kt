package com.example.explory.presentation.screen.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.explory.R
import com.example.explory.data.model.Friend
import com.example.explory.presentation.screen.friends.FriendsScreen
import com.example.explory.ui.theme.BlackButtonColor
import com.example.explory.ui.theme.DisabledBlackButtonColor
import com.example.explory.ui.theme.DisabledWhiteContentColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userName: String,
    userStatus: String,
    state: Int,
    onBackClick: () -> Unit,
    onInviteFriends: () -> Unit,
    onSettingsClick: () -> Unit,
    friends: List<Friend>
) {
    val sheetState = rememberModalBottomSheetState()

//    TopAppBar(
//        title = { Text(text = "") },
//        navigationIcon = {
//            IconButton(onClick = onBackClick) {
//                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//            }
//        }
//    )

    ModalBottomSheet(
        onDismissRequest = { onBackClick() },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.DarkGray, shape = CircleShape)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.picture),
                        contentDescription = null,
                        modifier = Modifier
                            .size(64.dp)
                            .align(Alignment.Center),
                        tint = Color.White
                    )
                }
                Button(
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = BlackButtonColor,
                        disabledContentColor = DisabledWhiteContentColor,
                        disabledContainerColor = DisabledBlackButtonColor
                    ),
                    border = BorderStroke(1.dp, Color.White),
                    onClick = { /* TODO: Добавить действие */ }
                ) {
                    Text(text = "Редактировать")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = userName, color = Color.White, style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = userStatus, color = Color.Gray, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))

            SemiRoundedButtonsRow(
                onFirstButtonClick = { /*TODO*/ },
                onSecondButtonClick = { /*TODO*/ },
                onThirdButtonClick = { }
            )

            when (state)
            {
                1 -> FriendsScreen(
                    friends = friends,
                    onInviteFriends = { /*TODO*/ }
                )
            }
        }
    }
}