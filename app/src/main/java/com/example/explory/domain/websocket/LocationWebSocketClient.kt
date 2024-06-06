package com.example.explory.domain.websocket

import android.util.Log
import com.example.explory.data.model.location.LocationRequest
import com.example.explory.data.model.location.LocationResponse
import com.example.explory.domain.usecase.GetUserTokenUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class LocationWebSocketClient(
    private val url: String,
    private val getUserTokenUseCase: GetUserTokenUseCase,
    private val interceptor: Interceptor
) {
    private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

    private val _messages = MutableStateFlow<LocationResponse?>(null)
    val messages: StateFlow<LocationResponse?> get() = _messages

    private lateinit var webSocket: WebSocket
    private var reconnectAttempts = 0
    private val maxReconnectAttempts = 15
    private val reconnectInterval = 5000L

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun connect() {
        val token = getUserTokenUseCase.execute()
        Log.d("Token", token.toString())
        val request = Request.Builder()
            .url(url)
//            .addHeader("Authorization", "Bearer $token")
            .build()

        webSocket = client.newWebSocket(request, createWebSocketListener())
    }

    fun sendLocationRequest(locationRequest: LocationRequest) {
        val jsonRequest = Json.encodeToString(locationRequest)
        Log.d("Sending coordinates", jsonRequest)
        webSocket.send(jsonRequest)
    }

    fun close() {
        webSocket.close(WebSocketListenerImpl.NORMAL_CLOSURE_STATUS, null)
        client.dispatcher.executorService.shutdown()
    }

    private fun createWebSocketListener() = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d("Connected", "WebSocket connected")
            reconnectAttempts = 0
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("Received polygons", text)
            try {
                val response = Json.decodeFromString<LocationResponse>(text)
                _messages.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.d("Connection failed", t.message.toString())
            attemptReconnect()
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            println("WebSocket Closing: $reason")
            webSocket.close(WebSocketListenerImpl.NORMAL_CLOSURE_STATUS, null)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            println("WebSocket Closed: $reason")
        }
    }

    private fun attemptReconnect() {
        if (reconnectAttempts < maxReconnectAttempts) {
            reconnectAttempts++
            coroutineScope.launch {
                delay(reconnectInterval)
                Log.d("Reconnecting attempt", "Attempt $reconnectAttempts")
                connect()
            }
        } else {
            Log.d("Reconnecting failed", "Max reconnect attempts reached")
            close()
        }
    }

    companion object WebSocketListenerImpl {
        const val NORMAL_CLOSURE_STATUS = 1000
    }
}
