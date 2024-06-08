package com.example.explory.presentation.screen.quest

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.explory.R
import com.example.explory.data.service.PointDto
import com.example.explory.ui.theme.AccentColor
import com.example.explory.ui.theme.White
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotationGroup
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions

@OptIn(ExperimentalMaterial3Api::class, MapboxExperimental::class)
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
        MapboxMap(
            Modifier.fillMaxHeight(0.7f),
            mapViewportState = remember {
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
            },
            style = {
                MapStyle(style = Style.STANDARD)
            }
        ) {
            // todo

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
                            PolylineAnnotationOptions()
                                .withLineColor(AccentColor.toArgb())
                                .withLineWidth(4.0)
                                .withPoints(
                                    listOf(
                                        Point.fromLngLat(
                                            pointDto.longitude.toDouble(),
                                            pointDto.latitude.toDouble()
                                        ),
                                        Point.fromLngLat(
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
        Box {
            Column {
                Text(text = name)
                Text(text = description)
                Text(text = "Сложность $difficulty")
                Text(text = "Тип транспорта $transportType")
                Text(text = "Расстояние $distance")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewP2P() {
}
