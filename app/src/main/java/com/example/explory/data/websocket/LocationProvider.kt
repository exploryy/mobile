package com.example.explory.data.websocket

import android.location.Location


interface LocationProvider {
    fun startLocationUpdates(callback: (Location) -> Unit)
}