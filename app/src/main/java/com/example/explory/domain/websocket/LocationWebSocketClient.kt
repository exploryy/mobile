package com.example.explory.domain.websocket

import android.util.Log
import com.example.explory.data.model.location.LocationRequest
import com.example.explory.data.model.location.LocationResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class LocationWebSocketClient(
    private val url: String,
    private val token: String,
) {
    private val client = OkHttpClient()
    private lateinit var webSocket: WebSocket

    fun connect() {
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $token")
            .build()

        webSocket = client.newWebSocket(request, createWebSocketListener())
        Log.d("Подключено", "Я тут")
    }

    fun sendLocationRequest(locationRequest: LocationRequest) {
        //val jsonRequest = Json.encodeToString(locationRequest)
        //webSocket.send(jsonRequest)
    }

    fun close() {
        webSocket.close(WebSocketListenerImpl.NORMAL_CLOSURE_STATUS, null)
        client.dispatcher.executorService.shutdown()
    }

    private fun createWebSocketListener() = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            println("WebSocket Connected")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            val response = Json.decodeFromString<LocationResponse>(text)
            Log.d("fdsfd", response.geo)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.d("Все плохо", t.message.toString())
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            println("WebSocket Closing: $reason")
            webSocket.close(WebSocketListenerImpl.NORMAL_CLOSURE_STATUS, null)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            println("WebSocket Closed: $reason")
        }
    }

    companion object WebSocketListenerImpl {
        const val NORMAL_CLOSURE_STATUS = 1000
    }
}