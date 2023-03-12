package com.example.canvasrecorderwithffmpeg

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.IBinder
import android.util.Log
import com.example.canvasrecorderwithffmpeg.MainActivity

class MediaProjectionService : Service() {
	private val TAG = "MediaProjectionService"
	private var mediaProjection: MediaProjection? = null
	private var mediaProjectionManager: MediaProjectionManager? = null

	override fun onBind(intent: Intent?): IBinder? {
		return null
	}

	override fun onCreate() {
		super.onCreate()
		Log.d(TAG, "onCreate()")
		mediaProjectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
	}

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		Log.d(TAG, "onStartCommand()")

		if (intent == null) {
			stopSelf()
			return START_NOT_STICKY
		}

		val resultCode = intent.getIntExtra("resultCode", 0)
		val data = intent.getParcelableExtra<Intent>("data")

		if (mediaProjection == null) {
			mediaProjection = mediaProjectionManager?.getMediaProjection(resultCode, data!!)
			if (mediaProjection != null) {
				startForeground(1, createNotification())
			}
		}

		return START_NOT_STICKY
	}

	override fun onDestroy() {
		super.onDestroy()
		Log.d(TAG, "onDestroy()")
		mediaProjection?.stop()
		stopForeground(true)
	}

	private fun createNotification(): Notification {
		val notificationIntent = Intent(this, MainActivity::class.java)
		val pendingIntent = PendingIntent.getActivity(
			this, 0,
			notificationIntent, 0
		)

		return Notification.Builder(this, "default")
			.setContentTitle("Media Projection Service")
			.setContentText("Recording your screen")
			.setSmallIcon(R.drawable.ic_launcher_foreground)
			.setContentIntent(pendingIntent)
			.setTicker("Media Projection Service")
			.build()
	}
}