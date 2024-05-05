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
        map.mapType = MapType.VECTOR_MAP
        val cameraListener =
            CameraListener { map, cameraPosition, cameraUpdateSource, finished ->
                Log.d(
                    "MapYandex",
                    "onCameraPositionChanged: $cameraPosition"
                )
            }
        map.addCameraListener(cameraListener)

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
            Animation(Animation.Type.SMOOTH, 2f),
            null
        )
    }

    fun drawCircle(latitude: Double, longitude: Double) {
        Log.d("MapYandex", "drawCircle: $latitude, $longitude")
        val userPoints = listOf(
            Point(latitude - 0.01, longitude - 0.01),
            Point(latitude - 0.01, longitude + 0.01),
            Point(latitude + 0.01, longitude + 0.01),
            Point(latitude + 0.01, longitude - 0.01),
        )
        val mapPoints = listOf(
            Point(-85.1054596961173, -180.0),
            Point(85.1054596961173, -180.0),
            Point(85.1054596961173, 180.0),
            Point(-85.1054596961173, 180.0),
            Point(-85.1054596961173, 0.0),
        )
        val polygon = Polygon(LinearRing(mapPoints), listOf(LinearRing(userPoints)))
        map.mapObjects.addPolygon(polygon).apply {
            fillColor = Black.toArgb()
            strokeColor = White.toArgb()
            strokeWidth = 2f
        }
//        val circle = Circle(Point(latitude, longitude), 100.0f)
//        map.mapObjects.addCircle(circle).apply {
//            fillColor = White.toArgb()
//            strokeColor = White.toArgb()
//            strokeWidth = 0f
//        }
    }


}

