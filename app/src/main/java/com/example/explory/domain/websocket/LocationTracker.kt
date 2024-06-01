package com.example.explory.domain.websocket

import android.location.Location

class LocationTracker(
    private val locationProvider: LocationProvider
) {

    private var locationListener: ((Location) -> Unit)? = null

    fun startTracking() {
        locationProvider.getLocation { location ->
            locationListener?.invoke(location)
        }
    }

    fun stopTracking() {
        locationListener = null
    }

    fun setLocationListener(listener: (Location) -> Unit) {
        locationListener = listener
    }
}