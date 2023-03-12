package com.example.canvasrecorderwithffmpeg

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MyForegroundService : Service() {

	private val NOTIFICATION_ID = 123
	private val CHANNEL_ID = "ForegroundServiceChannel"

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		val notificationIntent = Intent(this, MainActivity::class.java)
		val pendingIntent = PendingIntent.getActivity(this,
			0, notificationIntent, 0)

		val notification = NotificationCompat.Builder(this, CHANNEL_ID)
			.setContentTitle("Foreground Service")
			.setContentText("Service is running in foreground")
//			.setSmallIcon(R.drawable.ic_notification)
			.setContentIntent(pendingIntent)
			.build()

		startForeground(NOTIFICATION_ID, notification)

		return START_STICKY
	}

	override fun onBind(intent: Intent?): IBinder? {
		return null
	}



}