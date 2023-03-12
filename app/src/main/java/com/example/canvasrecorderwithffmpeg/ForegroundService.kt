package com.example.canvasrecorderwithffmpeg

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class ForegroundService : Service() {
	private val CHANNEL_ID = "ForegroundService Kotlin"

	companion object {
		fun startService(context: Context, message: String) {
			println("start service")
			val startIntent = Intent(context, ForegroundService::class.java)
			startIntent.putExtra("inputExtra", message)
			ContextCompat.startForegroundService(context, startIntent)
		}

		fun stopService(context: Context) {
			println("stop service")
			val stopIntent = Intent(context, ForegroundService::class.java)
			context.stopService(stopIntent)
		}
	}

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		println("on start command")
		//do heavy work on a background thread
		val input = intent?.getStringExtra("inputExtra")
		createNotificationChannel()
		val notificationIntent = Intent(this, MainActivity::class.java)
		val pendingIntent = PendingIntent.getActivity(
			this,
			0, notificationIntent, 0
		)

		val notification = NotificationCompat.Builder(this, CHANNEL_ID)
			.setContentTitle("Foreground Service Kotlin Example")
			.setContentText(input)
			.setSmallIcon(com.google.android.material.R.drawable.navigation_empty_icon)
			.setContentIntent(pendingIntent)
			.build()

		startForeground(1, notification)
		//stopSelf();
		return START_NOT_STICKY
	}

	override fun onBind(intent: Intent): IBinder? {
		return null
	}

	private fun createNotificationChannel() {
		println("create notification channel")
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val serviceChannel = NotificationChannel(CHANNEL_ID, "Foreground Service Channel",
				NotificationManager.IMPORTANCE_DEFAULT)

			val manager = getSystemService(NotificationManager::class.java)
			manager!!.createNotificationChannel(serviceChannel)
		}
	}
}