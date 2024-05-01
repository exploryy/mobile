package com.example.explory.presentation.map

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.explory.R
import ru.dgis.sdk.DGis
import ru.dgis.sdk.Duration
import ru.dgis.sdk.KeyFromAsset
import ru.dgis.sdk.KeySource
import ru.dgis.sdk.coordinates.Bearing
import ru.dgis.sdk.coordinates.GeoPoint
import ru.dgis.sdk.map.BearingSource
import ru.dgis.sdk.map.CameraAnimationType
import ru.dgis.sdk.map.CameraPosition
import ru.dgis.sdk.map.MapView
import ru.dgis.sdk.map.MyLocationController
import ru.dgis.sdk.map.MyLocationMapObjectSource
import ru.dgis.sdk.map.Tilt
import ru.dgis.sdk.map.Zoom
import ru.dgis.sdk.positioning.DefaultLocationSource
import ru.dgis.sdk.positioning.registerPlatformLocationSource

class MapViewLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val mapView: MapView
    private val sdkContext = DGis.initialize(
        context, keySource = KeySource(fromAsset = KeyFromAsset("dgissdk.key"))
    )

    init {
        LayoutInflater.from(context).inflate(R.layout.activity_map, this, true)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        val sdkContext = DGis.initialize(
//            context, keySource = KeySource(fromAsset = KeyFromAsset("dgissdk.key"))
//        )


        mapView = findViewById(R.id.mapView)
        // Создания источника данных о текущем местоположении
        val locationSource = DefaultLocationSource(context)

        // Регистрация источника данных в SDK
        registerPlatformLocationSource(sdkContext, locationSource)

        // Создание источника маркера геопозиции
//        val source = MyLocationMapObjectSource(
//            sdkContext, MyLocationController(BearingSource.SATELLITE)
//        )

        // Добавление источника маркера геопозиции на карту

//        mapView.getMapAsync { map ->
//            val cameraPosition = CameraPosition(
//                point = GeoPoint(latitude = 55.752425, longitude = 37.613983),
//                zoom = Zoom(16.0F),
//                tilt = Tilt(25.0F),
//                bearing = Bearing(85.0)
//            )
//
//            map.camera.move(cameraPosition, Duration.ofSeconds(2), CameraAnimationType.LINEAR)
//                .onResult {
//                    Log.d("APP", "Перелёт камеры завершён.")
//                }

//        }
    }

    fun moveCamera(latitude: Double, longitude: Double) {
        val locationSource = DefaultLocationSource(context)

// Регистрация источника данных в SDK
        registerPlatformLocationSource(sdkContext, locationSource)

// Создание источника маркера геопозиции
        val source = MyLocationMapObjectSource(
            sdkContext,
            MyLocationController(BearingSource.SATELLITE)
        )
//        val source = MyLocationMapObjectSource(
//            sdkContext, MyLocationController(BearingSource.SATELLITE)
//        )
        mapView.getMapAsync { map ->
            Log.d("APP", "Карта готова к использованию.")
            Log.d(
                "APP",
                "Перемещение камеры к местоположению пользователя {latitude: $latitude, longitude: $longitude}."
            )
            val cameraPosition = CameraPosition(
                point = GeoPoint(latitude = latitude, longitude = longitude),
                zoom = Zoom(16.0F),
//                tilt = Tilt(25.0F),
                bearing = Bearing(85.0)
            )

//            map.camera.position = cameraPosition
//
            map.camera.move(cameraPosition, Duration.ofSeconds(2), CameraAnimationType.LINEAR)
                .onResult {
                    Log.d("APP", "Перелёт камеры завершён.")
                }
            map.addSource(source)
        }
    }
}