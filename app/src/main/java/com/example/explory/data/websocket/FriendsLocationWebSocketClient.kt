package com.example.explory.data.websocket

import android.util.Log
import com.example.explory.data.model.location.CreatePolygonRequestDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class FriendsLocationWebSocketClient(
    private val baseUrl: String,
    private val interceptor: Interceptor
) {
    private val _messages = MutableStateFlow<CreatePolygonRequestDto?>(null)
    val messages: StateFlow<CreatePolygonRequestDto?> get() = _messages

    @Volatile
    private var webSocket: WebSocket? = null
    private var reconnectAttempts = 0
    private val maxReconnectAttempts = 15
    private val reconnectInterval = 5000L

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val connectionLock = Any()

    private fun createClient() = OkHttpClient.Builder().addInterceptor(interceptor).build()

    fun connect() {
        val url = baseUrl
        Log.d("WebSocket URL", url)
        val request = Request.Builder()
            .url(url)
            .build()

        synchronized(connectionLock) {
            if (webSocket != null) {
                Log.d("WebSocket", "Already connected or connecting")
                return
            }

            val client = createClient()
            webSocket = client.newWebSocket(request, createWebSocketListener(client))
        }
    }

    fun close() {
        synchronized(connectionLock) {
            webSocket?.close(NORMAL_CLOSURE_STATUS, "Closing Friend WebSocket")
            webSocket = null
        }
    }

    private fun createWebSocketListener(client: OkHttpClient) = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d("Connected", "Friend WebSocket connected")
            reconnectAttempts = 0
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("Received friend location", text)
            try {
                val response = Json.decodeFromString<CreatePolygonRequestDto>(
                    text.dropLast(1).drop(1).replace("\\", "")
                )
                _messages.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.d("Friends Connection failed", t.message.toString())
            client.dispatcher.executorService.shutdown()
            attemptReconnect()
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("Friend WebSocket Closing", reason)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("Friend WebSocket Closed", reason)
        }
    }

    private fun attemptReconnect() {
        synchronized(connectionLock) {
            if (webSocket != null) {
                webSocket?.close(NORMAL_CLOSURE_STATUS, "Friend Reconnecting")
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
                Log.d("Reconnecting failed", "Max reconnect attempts reached")
                close()
            }
        }
    }

    companion object WebSocketListenerImpl {
        const val NORMAL_CLOSURE_STATUS = 1000
    }
}
