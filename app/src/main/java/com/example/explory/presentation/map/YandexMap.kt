package com.example.explory.presentation.map

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.explory.R
import com.example.explory.ui.theme.Black
import com.example.explory.ui.theme.Transparent
import com.example.explory.ui.theme.White
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Circle
import com.yandex.mapkit.geometry.LinearRing
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polygon
import com.yandex.mapkit.images.DefaultImageUrlProvider
import com.yandex.mapkit.layers.LayerOptions
import com.yandex.mapkit.layers.TileFormat
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.CreateTileDataSource
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapType
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.tiles.UrlProvider


class MapYandex @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val mapView: MapView
    private val map: Map
        get() = mapView.mapWindow.map
    val myLocationButton: Button


    init {
        LayoutInflater.from(context).inflate(R.layout.map_yandex, this, true)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.yandex_mapview)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        MapKitFactory.initialize(context)
        mapView = findViewById(R.id.yandex_mapview)
        MapKitFactory.getInstance().onStart()
        val layerOptions = LayerOptions().apply {
            versionSupport = false
        }
        val LOGO_URL = "https://maps-ios-pods-public.s3.yandex.net/mapkit_logo.png"
        val urlProvider = UrlProvider { tileId, version, features ->
            LOGO_URL
        }
        val imageUrlProvider = DefaultImageUrlProvider()
//        map.addTileLayer(
//            "map",
//            layerOptions
//        ) { tile ->
//            tile.setTileFormat(TileFormat.PNG)
//            tile.setTileUrlProvider(urlProvider)
//            tile.setImageUrlProvider(imageUrlProvider)
//        }
//        map.mapType = MapType.VECTOR_MAP
//        val cameraListener =
//            CameraListener { map, cameraPosition, cameraUpdateSource, finished ->
//                if (cameraPosition.zoom < 16 && cameraUpdateSource == CameraUpdateReason.GESTURES) {
//                    map.move(
//                        CameraPosition(
//                            cameraPosition.target,
//                            /* zoom = */ 16.2f,
//                            /* azimuth = */ cameraPosition.azimuth,
//                            /* tilt = */ cameraPosition.tilt
//                        ),
//                        Animation(Animation.Type.SMOOTH, 0.5f),
//                        null
//                    )
//                }
//                if (cameraPosition.zoom > 16.5 && cameraUpdateSource == CameraUpdateReason.GESTURES) {
//                    map.move(
//                        CameraPosition(
//                            cameraPosition.target,
//                            /* zoom = */ 16.499f,
//                            /* azimuth = */ cameraPosition.azimuth,
//                            /* tilt = */ cameraPosition.tilt
//                        ),
//                        Animation(Animation.Type.SMOOTH, 0.5f),
//                        null
//                    )
//                }
//            }
//        map.addCameraListener(cameraListener)

        myLocationButton = findViewById(R.id.my_location_button)
        mapView.onStart()
    }


    fun moveTo(latitude: Double, longitude: Double) {
        Log.d("MapYandex", "moveTo: $latitude, $longitude")
        map.move(
            CameraPosition(
                Point(latitude, longitude),
                /* zoom = */ 17.0f,
                /* azimuth = */ 150.0f,
                /* tilt = */ 0f
            ),
//            Animation(Animation.Type.SMOOTH, 2f),
//            null
        )
    }

    fun drawCircle(latitude: Double, longitude: Double, coordinates: List<List<List<List<Double>>>>) {
        Log.d("MapYandex", "drawCircle: $latitude, $longitude")

        val userPoints = listOf(
            Point(latitude - 0.01, longitude - 0.01),
            Point(latitude - 0.01, longitude + 0.01),
            Point(latitude + 0.01, longitude + 0.01),
            Point(latitude + 0.01, longitude - 0.01),
        )
        for (coordinate in coordinates) {
            val mapPoints = coordinate[0].map { Point(it[1], it[0]) }
            val polygon = Polygon(LinearRing(mapPoints), listOf(LinearRing(userPoints)))
            Log.d("MapYandex", "drawCircle polygon: $polygon")
            map.mapObjects.addPolygon(polygon).apply {
                fillColor = Black.toArgb()
                strokeColor = Transparent.toArgb()
                strokeWidth = 0f
            }
        }
//        val polygon = Polygon(LinearRing(mapPoints), listOf(LinearRing(userPoints)))
//        map.mapObjects.addPolygon(polygon).apply {
//            fillColor = Black.toArgb()
//            strokeColor = Transparent.toArgb()
//            strokeWidth = 0f
//        }
    }

//    fun drawRussia(coordinates: List<List<List<List<Double>>>>) {
//        Log.d("MapYandex", "drawRussia $coordinates")
//        for (coordinate in coordinates) {
//            val mapPoints = coordinate[0].map { Point(it[1], it[0]) }
//            val polygon = Polygon(LinearRing(mapPoints), emptyList())
//            Log.d("MapYandex", "drawRussia polygon: $polygon")
//            map.mapObjects.addPolygon(polygon).apply {
//                fillColor = Black.toArgb()
//                strokeColor = Transparent.toArgb()
//                strokeWidth = 0f
//            }
//        }
//    }


}

