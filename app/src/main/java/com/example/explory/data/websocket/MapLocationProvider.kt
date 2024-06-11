package com.example.explory.data.websocket

import android.location.Location
import com.example.explory.foreground.LocationClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MapLocationProvider(private val client: LocationClient) : LocationProvider {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var callback: ((Location) -> Unit)? = null

    override fun startLocationUpdates(callback: (Location) -> Unit) {
        this.callback = callback
        try {
            client.getLocationUpdates(5000L).onEach {
                callback(it)
            }.launchIn(serviceScope)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}