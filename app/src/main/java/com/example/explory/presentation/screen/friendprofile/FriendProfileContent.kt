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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.explory.R
import com.example.explory.data.model.inventory.CosmeticItemInInventoryDto
import com.example.explory.data.model.statistic.UserStatisticDto
import com.example.explory.presentation.common.Avatar
import com.example.explory.presentation.screen.userstatistic.component.UserStatisticContent
import com.example.explory.ui.theme.Black
import com.example.explory.ui.theme.S18_W600
import com.example.explory.ui.theme.S20_W600
import com.example.explory.ui.theme.White

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