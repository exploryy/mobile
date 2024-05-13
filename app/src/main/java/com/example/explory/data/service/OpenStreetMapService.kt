package com.example.explory.data.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenStreetMapService {
    @GET("/get_geojson.py?id=140295&params=0")
    suspend fun getWordContent(): Response<GeoJsonResponse>
}

class GeoJsonResponse(
    val type: String,
    val coordinates: List<List<List<List<Double>>>>
)