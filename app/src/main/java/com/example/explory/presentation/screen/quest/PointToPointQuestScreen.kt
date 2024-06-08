package com.example.explory.presentation.screen.quest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.explory.R
import com.example.explory.data.service.PointDto
import com.example.explory.ui.theme.AccentColor
import com.example.explory.ui.theme.Black
import com.example.explory.ui.theme.DarkGray
import com.example.explory.ui.theme.MediumGray
import com.example.explory.ui.theme.Red
import com.example.explory.ui.theme.S14_W600
import com.example.explory.ui.theme.S16_W400
import com.example.explory.ui.theme.S24_W600
import com.example.explory.ui.theme.White
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotationGroup
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.skydoves.flexible.bottomsheet.material3.FlexibleBottomSheet
import com.skydoves.flexible.core.FlexibleSheetSize
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState

@OptIn(MapboxExperimental::class, ExperimentalMaterial3Api::class)
@Composable
fun PointToPointQuestScreen(
    name: String,
    description: String,
    difficulty: String,
    transportType: String,
    distance: Long,
    image: String?,
    points: List<PointDto>,
    onBackNavigate: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        IconButton(
            onClick = onBackNavigate
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back),
                tint = White,
                contentDescription = null
            )
        }
        MapboxMap(Modifier.fillMaxSize(), mapViewportState = remember {
            MapViewportState().apply {
                setCameraOptions {
                    center(
                        Point.fromLngLat(
                            points[points.size / 2].longitude.toDouble(),
                            points[points.size / 2].latitude.toDouble()
                        )
                    )
                    zoom(15.0)
                }
            }
        }, style = {
            MapStyle(style = Style.STANDARD)
        }) {
            // todo сделать отправку координат в бекграунде (желательно сделать так, чтобы это записывалось в файл и только при заходе отдавалось!!

            /*
            не нужны некст поинты (может и нужны на самом деле, но если делать без них, то тоже работает)
            впадлу делать много изрбражений на квесте (одно норм)
            что такое квест транспорт ? (мы же вроде хотели сделать так, чтобы юзер выбирал, а тут получается приходит то, на чём нужно выполнить)
            зачем координаты в дто квеста на дистанцию (и почему их две в разных местах)
            в чём приходит дистанция? в метрах?)
            если всё же будут отзывы ( ну мне впадлу естественно ), то нужно чтоб они тоже приходили на детали квеста (или другой эндпоинт для этого)
            что у нас по трекингу квеста на поинт ту поинт? важно ли нам чтобы юзер прошёл по пути или это типо рекомендация
            что нужно от меня чтобы квест завершился
             */
            PolylineAnnotationGroup(annotations = mutableListOf<PolylineAnnotationOptions>().apply {
                points.forEachIndexed { index, pointDto ->
                    if (index < points.size - 1) {
                        add(
                            PolylineAnnotationOptions().withLineColor(AccentColor.toArgb())
                                .withLineWidth(4.0).withPoints(
                                    listOf(
                                        Point.fromLngLat(
                                            pointDto.longitude.toDouble(),
                                            pointDto.latitude.toDouble()
                                        ), Point.fromLngLat(
                                            points[index + 1].longitude.toDouble(),
                                            points[index + 1].latitude.toDouble()
                                        )
                                    )
                                )
                        )
                    }
                }
            })
        }
    }
    FlexibleBottomSheet(
        onDismissRequest = { },
        sheetState = rememberFlexibleBottomSheetState(
            flexibleSheetSize = FlexibleSheetSize(
                fullyExpanded = 0.9f,
                intermediatelyExpanded = 0.5f,
                slightlyExpanded = 0.15f,
            ),
            isModal = false,
            skipSlightlyExpanded = false,
            skipHiddenState = true,
        ),
        containerColor = Black,
    ) {
        Column {
            Text(text = name)
            Text(text = description)
            Text(text = "Сложность $difficulty")
            Text(text = "Тип транспорта $transportType")
            Text(text = "Расстояние $distance")
        }
    }
}


@Composable
fun P2PQuestSheet(
    name: String,
    image: String?,
    description: String,
    difficulty: Color,
    transportType: String,
    distance: Long
) {
    FlexibleBottomSheet(
        onDismissRequest = { },
        sheetState = rememberFlexibleBottomSheetState(
            flexibleSheetSize = FlexibleSheetSize(
                fullyExpanded = 0.9f,
                intermediatelyExpanded = 0.5f,
                slightlyExpanded = 0.2f,
            ),
            isModal = false,
            skipSlightlyExpanded = false,
            skipHiddenState = true,
        ),
        containerColor = DarkGray,
    ) {
        P2PContent(
            name = name,
            image = image,
            description = description,
            difficultyColor = difficulty,
            transportType = transportType,
            distance = distance
        )
    }
}

@Composable
fun P2PContent(
    name: String,
    image: String?,
    description: String,
    difficultyColor: Color,
    transportType: String,
    distance: Long
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(model = image, contentDescription = null, modifier = Modifier.size(64.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = name, style = S24_W600)
                Text(text = description, style = S16_W400, color = MediumGray)
            }
        }

        Text(
            text = "Рекомендуем выполнять $transportType",
            style = S16_W400,
            color = MediumGray
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Сложность", style = S16_W400, color = MediumGray)
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(shape = CircleShape)
                    .background(difficultyColor)
            )
        }
        Text(text = "Расстояние $distance метров", style = S16_W400, color = MediumGray)
        Spacer(modifier = Modifier.weight(1f))
        Button(
            shape = RoundedCornerShape(8.dp),
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
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
        difficultyColor = Red,
        transportType = "пешком",
        distance = 1000
    )

}
