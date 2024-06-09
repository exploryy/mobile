package com.example.explory.presentation.screen.quest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.explory.R
import com.example.explory.data.service.PointDto
import com.example.explory.ui.theme.Black
import com.example.explory.ui.theme.S14_W600
import com.example.explory.ui.theme.S16_W400
import com.example.explory.ui.theme.S20_W400
import com.example.explory.ui.theme.S24_W600
import com.example.explory.ui.theme.White

@Composable
fun P2PContent(
    name: String,
    point: PointDto,
    image: String?,
    description: String,
    difficulty: String,
    transportType: String,
    distance: Long
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(image)
                    .crossfade(true)
                    .build(),
                loading = {
                    CircularProgressIndicator()
                },
                error = {
                    Icon(
                        painter = painterResource(id = R.drawable.picture),
                        contentDescription = null
                    )
                },
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = "Квест", style = S24_W600)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = name, style = S20_W400)
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        InfoBox(text = "${point.latitude}, ${point.longitude}")
        Spacer(modifier = Modifier.height(18.dp))
        Text(text = description, style = S16_W400)
        Spacer(modifier = Modifier.height(18.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InfoBox(
                text = transportType
            )
            InfoBox(text = difficulty)
            InfoBox(text = "$distance метров")
        }
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            shape = RoundedCornerShape(8.dp),
            onClick = {},
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),

            colors = ButtonDefaults.buttonColors(
                contentColor = Black,
                containerColor = White
            )
        ) {
            Text("Начать", style = S14_W600)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview
@Composable
private fun PreviewP2P() {
    P2PContent(
        name = "Пойдём бухнем",
        image = null,
        description = "Давай сходим и прибухнем",
        point = PointDto(
            latitude = 55.7558.toString(),
            longitude = 37.6176.toString(),
            nextLatitude = 55.7558.toString(),
            nextLongitude = 37.6176.toString()
        ),
        difficulty = "легкий",
        transportType = "пешком",
        distance = 1000
    )

}