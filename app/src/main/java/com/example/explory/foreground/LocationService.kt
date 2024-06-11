package com.example.explory.foreground

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.explory.R
import com.example.explory.presentation.MainActivity

class LocationService : Service() {

//    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
//    private lateinit var locationClient: LocationClient

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

//    override fun onCreate() {
//        super.onCreate()
//        locationClient = DefaultLocationClient(
//            applicationContext,
//            LocationServices.getFusedLocationProviderClient(applicationContext)
//        )
//    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntentFlags =
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Explory works in background ...")
            .setSmallIcon(R.drawable.compass_alt)
            .setContentIntent(
                PendingIntent.getActivity(
                    this,
                    0,
                    intent,
                    pendingIntentFlags
                )
            )
            .setOngoing(true)

//        locationClient
//            .getLocationUpdates(5000L)
//            .catch { e -> e.printStackTrace() }
//            .launchIn(serviceScope)
        startForeground(1, notification.build())
    }

    private fun stop() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

//    override fun onDestroy() {
//        super.onDestroy()
////        serviceScope.cancel()
//    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}