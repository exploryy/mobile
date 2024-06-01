package com.example.explory.presentation.screen.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.explory.R
import com.example.explory.data.model.Friend
import com.example.explory.presentation.screen.friends.FriendsScreen
import com.example.explory.ui.theme.BlackButtonColor
import com.example.explory.ui.theme.DisabledBlackButtonColor
import com.example.explory.ui.theme.DisabledWhiteContentColor
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    state: Int,
    onBackClick: () -> Unit,
    onInviteFriends: () -> Unit,
    onSettingsClick: () -> Unit,
    friends: List<Friend>
) {
    val sheetState = rememberModalBottomSheetState()
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()

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
                //val imageUrl = profileState.profile?.avatarUri
                val imageUrl = "https://news.store.rambler.ru/img/e5af3f463d7644045782f62b91f61d56?img-format=auto&img-1-resize=height:400,fit:max&img-2-filter=sharpen"
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

            profileState.profile?.let { Text(text = it.name, color = Color.White, style = MaterialTheme.typography.headlineLarge) }
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Адыхает", color = Color.Gray, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))

            SemiRoundedButtonsRow(
                onFirstButtonClick = {  },
                onSecondButtonClick = {  },
                onThirdButtonClick = { }
            )

            when (state)
            {
                1 -> FriendsScreen(
                    friends = friends,
                    onInviteFriends = {  }
                )
            }
        }
    }
}