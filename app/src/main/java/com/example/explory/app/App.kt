package com.example.explory.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.explory.di.appModule
import com.example.explory.di.dataModule
import com.example.explory.di.domainModule
import com.example.explory.foreground.LocationService
import com.example.explory.foreground.hasNotificationPermission
import com.example.explory.foreground.hasOldNotificationPermission
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Log.d("App", "Checking notification permission")
                Log.d("App", "Permission status: ${this.hasNotificationPermission()}")
                this.hasNotificationPermission()
            } else {
                Log.d("App", "Checking old notification permission")
                this.hasOldNotificationPermission()
            }
        ) {
            Log.d("App", "Starting location service")
            val channel = NotificationChannel(
                "location",
                "Location",
                NotificationManager.IMPORTANCE_MIN
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            Intent(applicationContext, LocationService::class.java).apply {
                action = LocationService.ACTION_START
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(this)
                } else {
                    startService(this)
                }
            }
        }
        Log.d("App", "Starting Koin")
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(appModule(), dataModule(), domainModule()))
        }
    }
}