package com.example.canvasrecorderwithffmpeg

import android.app.Activity
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.SurfaceTexture
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Surface
import android.view.TextureView
import androidx.core.content.ContextCompat
import java.io.File
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : Activity(), TextureView.SurfaceTextureListener {
	private val videoUri = Uri.parse("https://kenangan.s3-ap-southeast-1.amazonaws.com/content-cache/video/43531af3-4e31-42dd-b40c-0e18901e892b.mp4")
//	private val imageUri = URL("https://kenangan.s3-ap-southeast-1.amazonaws.com/1673839532871-1673838518832-s2.png")
	private lateinit var mMediaPlayer: MediaPlayer
	private lateinit var mTextureView: TextureView
	private lateinit var mMediaRecorder: MediaRecorder

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		mMediaPlayer = MediaPlayer()
		mMediaRecorder = MediaRecorder()

		mTextureView = TextureView(this)
		mTextureView.surfaceTextureListener = this
		setContentView(mTextureView)
	}

	override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
		try {
			mMediaPlayer.setDataSource(this, videoUri)
			mMediaPlayer.setSurface(Surface(surfaceTexture))
			mMediaPlayer.prepareAsync()
			mMediaPlayer.setOnPreparedListener { mp: MediaPlayer ->
				mp.start()
				mMediaRecorder.start()
			}
			mMediaPlayer.setOnCompletionListener {
				mMediaPlayer.stop()
				mMediaPlayer.release()
				mMediaRecorder.stop()
				mMediaRecorder.release()
			}
			mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE)
			mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
			mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
			mMediaRecorder.setVideoEncodingBitRate(512 * 1000)
			mMediaRecorder.setVideoFrameRate(30)
			mMediaRecorder.setVideoSize(640, 480)
			mMediaRecorder.setOutputFile(getOutputMediaFile())
			mMediaRecorder.setPreviewDisplay(Surface(surfaceTexture))
			mMediaRecorder.prepare()

			// Get the drawable you want to convert to a bitmap
//			val drawable: Drawable? = ContextCompat.getDrawable(this, R.drawable.test_image)
//
//			// Check if the drawable is not null
//			if (drawable != null) {
//				println("createing bit map")
//
//				// Convert the drawable to a bitmap
//				val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
//				val canvas = Canvas(bitmap)
//				drawable.setBounds(0, 0, canvas.width, canvas.height)
//				drawable.draw(canvas)
//
//				val surface = Surface(surfaceTexture)
//				val surfaceCanvas = surface.lockCanvas(null)
//
//				surfaceCanvas?.drawBitmap(bitmap, 0f, 0f, null)
//				surface.unlockCanvasAndPost(surfaceCanvas)
//			}


//			val bitmap = BitmapFactory.decodeStream(URL("https://kenangan.s3-ap-southeast-1.amazonaws.com/1673839532871-1673838518832-s2.png").openStream())
//			mTextureView.surfaceTexture!!.setDefaultBufferSize(bitmap.width, bitmap.height)
//			val surface = Surface(surfaceTexture)
//			val surfaceCanvas = surface.lockCanvas(null)
//			surfaceCanvas?.drawBitmap(bitmap, 0f, 0f, null)
//			surface.unlockCanvasAndPost(surfaceCanvas)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	override fun onSurfaceTextureSizeChanged(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
		// Handle size change depending on media needs
	}

	override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
		// Release unneeded resources
		mMediaPlayer.stop()
		mMediaPlayer.release()
		mMediaRecorder.stop()
		mMediaRecorder.release()
		return true
	}

	override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {
		// Invoked every time there's a new video frame
	}

	private fun getOutputMediaFile(): File {
		// Create a media file name
		val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
		val mediaFile = File(
			Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
			"MEDIA_PROJECTION_${timeStamp}.mp4"
		)
		return mediaFile
	}
}
