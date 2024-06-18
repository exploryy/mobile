package com.example.explory

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.explory.foreground.LocationService
import com.example.explory.presentation.navigation.AppNavigation
import com.example.explory.ui.theme.ExploryTheme
import com.mapbox.common.MapboxOptions

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        MapboxOptions.accessToken = BuildConfig.MAPBOX_API_KEY
        setContent {
            ExploryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Intent(applicationContext, LocationService::class.java).apply {
            action = LocationService.ACTION_STOP
            stopService(this)
        }
    }
}
