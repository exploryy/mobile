package com.example.explory.data.websocket

import android.util.Log
import com.example.explory.data.model.location.LocationRequest
import com.example.explory.data.model.location.LocationResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    private val interceptor: Interceptor
) {
    private val _messages = MutableStateFlow<LocationResponse?>(null)
    val messages: StateFlow<LocationResponse?> get() = _messages

    //    private val _error = MutableSharedFlow<String?>(
//        replay = 1,
//        extraBufferCapacity = 1
//    )
    private val _isConnected =
        MutableSharedFlow<Boolean?>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val isConnected = _isConnected.asSharedFlow()

    @Volatile
    private var webSocket: WebSocket? = null
    private var reconnectAttempts = 0
    private val maxReconnectAttempts = 5
    private val reconnectInterval = 5000L

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val connectionLock = Any()

    private fun createClient() = OkHttpClient.Builder().addInterceptor(interceptor).build()

    fun connect() {
        val request = Request.Builder()
            .url(url)
            .build()

        synchronized(connectionLock) {
            if (webSocket != null) {
                return
            }

            val client = createClient()
            webSocket = client.newWebSocket(request, createWebSocketListener(client))
        }
    }

    fun sendLocationRequest(locationRequest: LocationRequest) {
        val jsonRequest = Json.encodeToString(locationRequest)
        Log.d("Sending coordinates", jsonRequest)
        webSocket?.send(jsonRequest)
    }

    fun close() {
        synchronized(connectionLock) {
            webSocket?.close(NORMAL_CLOSURE_STATUS, "Closing WebSocket")
            webSocket = null
        }
    }

    private fun createWebSocketListener(client: OkHttpClient) = object : WebSocketListener() {
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
            _isConnected.tryEmit(false)
            client.dispatcher.executorService.shutdown()  // Properly shut down the dispatcher
            attemptReconnect()
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("WebSocket Closing", reason)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("WebSocket Closed", reason)
        }
    }

    private fun attemptReconnect() {
        synchronized(connectionLock) {
            if (webSocket != null) {
                webSocket?.close(NORMAL_CLOSURE_STATUS, "Reconnecting")
                webSocket = null
            }

            if (reconnectAttempts < maxReconnectAttempts) {
                reconnectAttempts++
                coroutineScope.launch {
                    delay(reconnectInterval)
                    Log.d("Reconnecting attempt", "Attempt $reconnectAttempts")
                    connect()
                }
            } else {
                _isConnected.tryEmit(true)
                Log.d("Reconnecting failed", "Max reconnect attempts reached")
                close()
            }
        }
    }

    companion object WebSocketListenerImpl {
        const val NORMAL_CLOSURE_STATUS = 1000
    }
}



