package com.example.explory.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.explory.di.appModule
import com.example.explory.di.dataModule
import com.example.explory.di.domainModule
import com.example.explory.foreground.LocationService
import com.example.explory.foreground.hasNotificationPermission
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                this.hasNotificationPermission()
            } else {
                true
//                this.hasOldNotificationPermission()
            }
        ) {
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
                startForegroundService(this)
            }
        }
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(appModule(), dataModule(), domainModule()))
        }
    }
}