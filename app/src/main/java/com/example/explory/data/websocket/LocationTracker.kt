package com.example.explory.data.websocket

import android.location.Location

class LocationTracker(
    private val locationProvider: LocationProvider,
) {
    private var locationListener: ((Location) -> Unit)? = null

    fun startTracking() {
        locationProvider.startLocationUpdates { location ->
            locationListener?.invoke(location)
        }
    }

    fun setLocationListener(listener: (Location) -> Unit) {
        locationListener = listener
    }
}