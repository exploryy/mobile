package com.example.explory.domain.websocket

import android.location.Location


interface LocationProvider {
    fun startLocationUpdates(callback: (Location) -> Unit)
    fun stopLocationUpdates()
}