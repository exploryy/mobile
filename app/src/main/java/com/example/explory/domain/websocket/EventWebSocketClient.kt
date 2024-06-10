package com.example.explory.domain.websocket

import android.util.Log
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class EventWebSocketClient(
    private val url: String,
    interceptor: Interceptor
) {
    private lateinit var webSocket: WebSocket
    private val _events = MutableSharedFlow<EventDto>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val client = OkHttpClient().newBuilder().addInterceptor(interceptor).build()
    val events = _events.asSharedFlow()
    private val _isConnected =
        MutableSharedFlow<Boolean?>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    fun connect() {
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, createWebSocketListener())
    }

    fun close() {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        client.dispatcher.executorService.shutdown()
    }

    private fun createWebSocketListener() = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d("EventWebSocket", "Connected")
            _isConnected.tryEmit(true)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("EventWebSocket", "Received: $text")
            try {
                val eventDto = Json.decodeFromString<EventDto>(text)
                Log.d("EventWebSocket", "Decoded: $eventDto")
                _events.tryEmit(eventDto)
            } catch (e: Exception) {
                Log.e("EventWebSocket", "Error decoding EventDto: ${e.message}")
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.d("EventWebSocket", "Error: ${t.message}")
            _isConnected.tryEmit(false)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("EventWebSocket", "Closing: $code / $reason")
            webSocket.close(NORMAL_CLOSURE_STATUS, null)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("EventWebSocket", "Closed: $code / $reason")
        }
    }

    companion object WebSocketListenerImpl {
        const val NORMAL_CLOSURE_STATUS = 1000
    }

}

@Serializable
data class EventDto(
    val text: String,
    val type: EventType
)

enum class EventType {
    COMPLETE_QUEST
}

