package com.example.explory.domain.websocket

import android.location.Location


interface LocationProvider {
    fun getLocation(callback: (Location) -> Unit)
}