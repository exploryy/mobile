package com.example.explory.domain.websocket

import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MapLocationProvider(private val context: Context) : LocationProvider {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    override fun getLocation(callback: (Location) -> Unit) {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                callback(location)
            }
        } catch (e: SecurityException) {
            //Что-то
        }
    }
}