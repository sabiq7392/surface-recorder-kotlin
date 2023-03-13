package com.example.canvasrecorderwithffmpeg

import android.content.Context
import android.content.Intent
import android.graphics.SurfaceTexture
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore.Audio.Media
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), SurfaceHolder.Callback {
    private lateinit var buttonStart: Button
    private lateinit var buttonStop: Button
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mediaRecorder: MediaRecorder
    private lateinit var surfaceView: SurfaceView
    private lateinit var surfaceHolder: SurfaceHolder
    private lateinit var startMediaProjection:  ActivityResultLauncher<Intent>
    private lateinit var mediaProjectionManager: MediaProjectionManager
    private lateinit var mediaProjection: MediaProjection
    private var virtualDisplay: VirtualDisplay? = null
    private val videoUri = Uri.parse("https://kenangan.s3-ap-southeast-1.amazonaws.com/content-cache/video/43531af3-4e31-42dd-b40c-0e18901e892b.mp4")
    private val REQUEST_MEDIA_PROJECTION = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonStop = findViewById(R.id.buttonStop)
        mediaPlayer = MediaPlayer()
        mediaRecorder = MediaRecorder()

				buttonStart = findViewById(R.id.buttonStart)
//				buttonStart.isEnabled = false

			surfaceView = findViewById(R.id.surface_view)
        surfaceHolder = surfaceView.holder
        surfaceHolder?.addCallback(this)


        ForegroundService.startService(this, "Foreground Service is running...")
        mediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(
            mediaProjectionManager.createScreenCaptureIntent(),
            REQUEST_MEDIA_PROJECTION
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        println("on activity result running...")
        super.onActivityResult(requestCode, resultCode, data)

        data ?: throw IllegalStateException("Intent cannot be null")

        println("on activity success to produce data...")
        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
//        mediaPlayer?.setDisplay(holder)
//        mediaPlayer?.setDataSource(this, videoUri)
//        mediaPlayer?.prepareAsync()

//				mediaPlayer.setOnPreparedListener {
//					println("video is already prepared...")
//					buttonStart.isEnabled = true
//				}

        buttonStart.setOnClickListener(View.OnClickListener {
            println("button start clicked...")
            val displayMetrics = resources.displayMetrics
						val width = 640
						val height = 480

            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE)
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            mediaRecorder.setVideoEncodingBitRate(512 * 1000)
            mediaRecorder.setVideoFrameRate(30)
            mediaRecorder.setVideoSize(width, height)
            mediaRecorder.setOutputFile(getOutputMediaFile())
            mediaRecorder.setPreviewDisplay(holder.surface)
						mediaRecorder.prepare()

            virtualDisplay = mediaProjection.createVirtualDisplay(
                "MainActivity",
//                displayMetrics.widthPixels,
//                displayMetrics.heightPixels,
                width,
                height,
                resources.displayMetrics.densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mediaRecorder.surface,
                null,
                null
            )

            mediaRecorder.start()
        })

        buttonStop.setOnClickListener() {
            stopRecording()
        }
    }
//
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        println("surface changed...")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        println("surface destroyed...")
        stopRecording()
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

    private fun stopRecording() {
//        mediaPlayer.stop()
        mediaRecorder.stop()
//        mediaRecorder.reset()
//        mediaRecorder.release()
//        mediaProjection.stop()
    }
}


//
//import android.Manifest
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.os.Bundle
//import android.widget.Button
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//
//class MainActivity: AppCompatActivity() {
//    private val REQ_PERMISSIONS = 1;
//    private lateinit var buttonStartRecording: Button;
//    private var isStarted: Boolean = false
//    private val permissions = arrayOf(
//        Manifest.permission.WRITE_EXTERNAL_STORAGE
//    )
//    private var mProjData: Intent? = null;
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        requestPermissions()
//
//        buttonStartRecording = findViewById(R.id.start_recording)
//        buttonStartRecording.setOnClickListener() {
//            isStarted = if (isStarted) {
//                buttonStartRecording.setText("Start Recording")
//                false
//            } else {
//                buttonStartRecording.setText("stop Recording")
//                true;
//            }
//        }
//    }
//
//
//    private fun requestPermissions() {
//        println("request permissions...")
//        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_DENIED) {
//            ActivityCompat.requestPermissions(this, permissions, REQ_PERMISSIONS)
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        println("on request permissions result...")
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if (requestCode == REQ_PERMISSIONS) {
//            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
//                finish()
//            }
//        }
//    }
//
//}


public class MainActiv

//
//import android.app.*
//import android.content.Context
//import android.content.Intent
//import android.hardware.display.DisplayManager
//import android.hardware.display.VirtualDisplay
//import android.media.MediaPlayer
//import android.media.MediaRecorder
//import android.media.projection.MediaProjection
//import android.media.projection.MediaProjectionManager
//import android.net.Uri
//import android.os.*
//import android.util.Log
//import android.view.SurfaceHolder
//import android.view.SurfaceView
//import android.widget.Button
//import androidx.activity.result.ActivityResultLauncher
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import java.io.File
//import java.text.SimpleDateFormat
//import java.util.*
//import java.util.jar.Manifest
//
//class MainActivity : AppCompatActivity() {
//
//    private val NOTIFICATION_ID = 123
//    private val CHANNEL_ID = "ForegroundServiceChannel"
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        createNotificationChannel()
//
//        val serviceIntent = Intent(this, MyForegroundService::class.java)
//        ContextCompat.startForegroundService(this, serviceIntent)
//    }
//
//    private fun createNotificationChannel() {
//        println("create notification chanel...")
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            println("jjjjasdsadsahoh")
//            val serviceChannel = NotificationChannel(
//                CHANNEL_ID,
//                "Foreground Service Channel",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//
//            val manager = getSystemService(NotificationManager::class.java)
//            manager.createNotificationChannel(serviceChannel)
//        }
//    }
//}

//class MainActivity : AppCompatActivity(), SurfaceHolder.Callback {
//    private var isRecording = false
//    private var virtualDisplay: VirtualDisplay? = null
//    private lateinit var mediaRecorder: MediaRecorder
//    private lateinit var mediaProjectionManager: MediaProjectionManager
//    private var mediaProjection: MediaProjection? = null
//    private val REQUEST_CODE_MEDIA_PROJECTION = 123
//    private var surfaceView: SurfaceView? =  null
//    private var surfaceHolder: SurfaceHolder? = null
//    private var mediaPlayer: MediaPlayer? = null
//    private val videoUri = Uri.parse("https://kenangan.s3-ap-southeast-1.amazonaws.com/content-cache/video/43531af3-4e31-42dd-b40c-0e18901e892b.mp4")
//    private lateinit var buttonStartRecording: Button
//    private lateinit var buttonStopRecording: Button
//    private var startMediaProjection:  ActivityResultLauncher<Intent>? = null
//    private var VIRTUAL_DISPLAY_NAME = "ScreenRecording"
//    private var permissions = arrayOf(
//        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//        android.Manifest.permission.RECORD_AUDIO,
//        android.Manifest.permission.FOREGROUND_SERVICE,
//        android.Manifest.permission.START_FOREGROUND_SERVICES_FROM_BACKGROUND,
//        android.Manifest.permission.REQUEST_COMPANION_START_FOREGROUND_SERVICES_FROM_BACKGROUND,
//    )
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        println("on create...")
//        setContentView(R.layout.activity_main)
//
//        surfaceView = findViewById(R.id.surface_view)
//        surfaceHolder = surfaceView?.holder
//        surfaceHolder?.addCallback(this)
//
//        mediaPlayer = MediaPlayer()
//        mediaRecorder = MediaRecorder()
//
//        buttonStartRecording = findViewById(R.id.start_recording)
//        buttonStartRecording.isEnabled = false
//
//        buttonStopRecording = findViewById(R.id.stop_recording)
//
//        val intent = Intent(this, MainActivity::class.java)
//        applicationContext.startForegroundService(intent)
//
////        val notification: Notification = Notification.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
////            .setContentTitle(getText(R.string.notification_title))
////            .setContentText(getText(R.string.notification_message))
////            .setSmallIcon(R.drawable.icon)
////            .setContentIntent(pendingIntent)
////            .setTicker(getText(R.string.ticker_text))
////            .build()
////
////        // Notification ID cannot be 0.
////        startForeground(ONGOING_NOTIFICATION_ID, notification)
//
//
////        mediaProjectionManager = getSystemService(MediaProjectionManager::class.java)
////        startMediaProjection  = registerForActivityResult(
////            ActivityResultContracts.StartActivityForResult()
////        ) {  result ->
////            println("get result registry activity...")
////            println(result)
////            println(result)
////            println(result)
////            println(result)
////            if (result.resultCode == RESULT_OK) {
////                mediaProjection = mediaProjectionManager
////                    .getMediaProjection(result.resultCode, result.data!!)
////            }
////        }
//    }
//
////    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
////        super.onActivityResult(requestCode, resultCode, data)
////
////        if (data == null) {
////            println("There is no data on activity result")
////        } else {
////            mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data!!)
////        }
////    }
//
//    override fun surfaceCreated(holder: SurfaceHolder) {
//        val displayMetrics = resources.displayMetrics
//        mediaPlayer?.setDisplay(holder)
//        mediaPlayer?.setDataSource(this, videoUri)
//        mediaPlayer?.prepareAsync()
//        mediaPlayer?.setOnPreparedListener {
//            println("video is prepared...")
//            buttonStartRecording.isEnabled = true
//        }
//
//        buttonStartRecording?.setOnClickListener() {
//            mediaPlayer?.start()
//            buttonStartRecording.isEnabled = false
////            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT)
////            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE)
////            val profile: CamcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH)
////            profile.videoFrameHeight = displayMetrics.heightPixels
////            profile.videoFrameWidth = displayMetrics.widthPixels
////            mediaRecorder.setProfile(profile)
////            mediaRecorder.setOutputFile(getOutputMediaFile())
////            mediaRecorder.prepare()
//            startMediaProjection?.launch(mediaProjectionManager.createScreenCaptureIntent())
//
////            println(DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR)
////            println(DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR)
////            println(DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR)
//
////            virtualDisplay = mediaProjection!!.createVirtualDisplay(
////                VIRTUAL_DISPLAY_NAME,
////                720,
////                1280,
////                displayMetrics.densityDpi,
////                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
////                holder.surface,
////                null,
////                null
////            )
//
////            mediaRecorder.apply {
////                setVideoSource(MediaRecorder.VideoSource.SURFACE)
////                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
////                setOutputFile(getOutputMediaFile().toString())
////                setVideoEncoder(MediaRecorder.VideoEncoder.H264)
////                setVideoSize(720, 1280)
////                setVideoFrameRate(30)
////                prepare()
////            }
//
//            // Create a virtual display
////            virtualDisplay = mediaProjection?.createVirtualDisplay(
////                "ScreenRecording",
////                720, 1280,
////                resources.displayMetrics.densityDpi,
////                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
////                holder.surface,
////                null,
////                null
////            )
//        }
//    }
//
//    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
//        println("surface changed...")
//    }
//
//    override fun surfaceDestroyed(holder: SurfaceHolder) {
//
//    }
//
//    private fun getOutputMediaFile(): File {
//        // Create a media file name
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val mediaFile = File(
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
//            "VID_${timeStamp}.mp4"
//        )
//        return mediaFile
//    }
//
//}
//


//import android.Manifest
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.hardware.display.DisplayManager
//import android.hardware.display.VirtualDisplay
//import android.media.MediaRecorder
//import android.media.projection.MediaProjection
//import android.media.projection.MediaProjectionManager
//import android.net.Uri
//import android.os.Bundle
//import android.os.Environment
//import android.provider.Settings
////import android.support.design.widget.Snackbar
////import android.support.v4.app.ActivityCompat
////import android.support.v7.app.AppCompatActivity
//import android.util.DisplayMetrics
//import android.util.Log
//import android.util.SparseIntArray
//import android.view.Surface
//import android.view.View
//import android.widget.Toast
//import android.widget.ToggleButton
//import androidx.annotation.NonNull
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.google.android.material.snackbar.Snackbar
//import java.io.IOException
//
//class MainActivity : AppCompatActivity() {
//    private var mScreenDensity = 0
//    private var mProjectionManager: MediaProjectionManager? = null
//    private var mMediaProjection: MediaProjection? = null
//    private var mVirtualDisplay: VirtualDisplay? = null
//    private var mMediaProjectionCallback: MediaProjectionCallback? = null
//    private var mToggleButton: ToggleButton? = null
//    private var mMediaRecorder: MediaRecorder? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        val metrics = DisplayMetrics()
//        getWindowManager().getDefaultDisplay().getMetrics(metrics)
//        mScreenDensity = metrics.densityDpi
//        mMediaRecorder = MediaRecorder()
//        mProjectionManager =
//            getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager?
//        mToggleButton = findViewById(R.id.toggle) as ToggleButton?
//        mToggleButton!!.setOnClickListener { v ->
//            if (ContextCompat.checkSelfPermission(
//                    this@MainActivity,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                ) + ContextCompat
//                    .checkSelfPermission(
//                        this@MainActivity,
//                        Manifest.permission.RECORD_AUDIO
//                    )
//                !== PackageManager.PERMISSION_GRANTED
//            ) {
//                if (ActivityCompat.shouldShowRequestPermissionRationale(
//                        this@MainActivity,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                    ) ||
//                    ActivityCompat.shouldShowRequestPermissionRationale(
//                        this@MainActivity,
//                        Manifest.permission.RECORD_AUDIO
//                    )
//                ) {
//                    mToggleButton!!.isChecked = false
//                    Snackbar.make(
//                        findViewById(android.R.id.content), R.string.label_permissions,
//                        Snackbar.LENGTH_INDEFINITE
//                    ).setAction("ENABLE",
//                        View.OnClickListener {
//                            ActivityCompat.requestPermissions(
//                                this@MainActivity,
//                                arrayOf<String>(
//                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                    Manifest.permission.RECORD_AUDIO
//                                ),
//                                REQUEST_PERMISSIONS
//                            )
//                        }).show()
//                } else {
//                    ActivityCompat.requestPermissions(
//                        this@MainActivity,
//                        arrayOf<String>(
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                            Manifest.permission.RECORD_AUDIO
//                        ),
//                        REQUEST_PERMISSIONS
//                    )
//                }
//            } else {
//                onToggleScreenShare(v)
//            }
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode != REQUEST_CODE) {
//            Log.e(TAG, "Unknown request code: $requestCode")
//            return
//        }
//        if (resultCode != RESULT_OK) {
//            Toast.makeText(
//                this,
//                "Screen Cast Permission Denied", Toast.LENGTH_SHORT
//            ).show()
//            mToggleButton!!.isChecked = false
//            return
//        }
//        mMediaProjectionCallback = MediaProjectionCallback()
//        mMediaProjection = mProjectionManager!!.getMediaProjection(resultCode, data!!)
//        mMediaProjection?.registerCallback(mMediaProjectionCallback, null)
//        mVirtualDisplay = createVirtualDisplay()
//        mMediaRecorder!!.start()
//    }
//
//    fun onToggleScreenShare(view: View?) {
//        if ((view as ToggleButton?)!!.isChecked) {
//            initRecorder()
//            shareScreen()
//        } else {
//            mMediaRecorder!!.stop()
//            mMediaRecorder!!.reset()
//            Log.v(TAG, "Stopping Recording")
//            stopScreenSharing()
//        }
//    }
//
//    private fun shareScreen() {
//        if (mMediaProjection == null) {
//            startActivityForResult(mProjectionManager!!.createScreenCaptureIntent(), REQUEST_CODE)
//            return
//        }
//        mVirtualDisplay = createVirtualDisplay()
//        mMediaRecorder!!.start()
//    }
//
//    private fun createVirtualDisplay(): VirtualDisplay {
//        return mMediaProjection!!.createVirtualDisplay(
//            "MainActivity",
//            DISPLAY_WIDTH, DISPLAY_HEIGHT, mScreenDensity,
//            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
//            mMediaRecorder!!.surface, null /*Callbacks*/, null /*Handler*/
//        )
//    }
//
//    private fun initRecorder() {
//        try {
//            mMediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
//            mMediaRecorder!!.setVideoSource(MediaRecorder.VideoSource.SURFACE)
//            mMediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
//            mMediaRecorder!!.setOutputFile(
//                Environment
//                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                    .toString() + "/video.mp4"
//            )
//            mMediaRecorder!!.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT)
//            mMediaRecorder!!.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
//            mMediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
//            mMediaRecorder!!.setVideoEncodingBitRate(512 * 1000)
//            mMediaRecorder!!.setVideoFrameRate(30)
//            val rotation: Int = getWindowManager().getDefaultDisplay().getRotation()
//            val orientation = ORIENTATIONS[rotation + 90]
//            mMediaRecorder!!.setOrientationHint(orientation)
//            mMediaRecorder!!.prepare()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
//
//    private inner class MediaProjectionCallback : MediaProjection.Callback() {
//        override fun onStop() {
//            if (mToggleButton!!.isChecked) {
//                mToggleButton!!.isChecked = false
//                mMediaRecorder!!.stop()
//                mMediaRecorder!!.reset()
//                Log.v(TAG, "Recording Stopped")
//            }
//            mMediaProjection = null
//            stopScreenSharing()
//        }
//    }
//
//    private fun stopScreenSharing() {
//        if (mVirtualDisplay == null) {
//            return
//        }
//        mVirtualDisplay!!.release()
//        //mMediaRecorder.release(); //If used: mMediaRecorder object cannot
//        // be reused again
//        destroyMediaProjection()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        destroyMediaProjection()
//    }
//
//    private fun destroyMediaProjection() {
//        if (mMediaProjection != null) {
//            mMediaProjection!!.unregisterCallback(mMediaProjectionCallback)
//            mMediaProjection!!.stop()
//            mMediaProjection = null
//        }
//        Log.i(TAG, "MediaProjection Stopped")
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            REQUEST_PERMISSIONS -> {
//                if (grantResults.size > 0 && grantResults[0] +
//                    grantResults[1] == PackageManager.PERMISSION_GRANTED
//                ) {
//                    onToggleScreenShare(mToggleButton)
//                } else {
//                    mToggleButton!!.isChecked = false
//                    Snackbar.make(
//                        findViewById(android.R.id.content), R.string.label_permissions,
//                        Snackbar.LENGTH_INDEFINITE
//                    ).setAction("ENABLE",
//                        View.OnClickListener {
//                            val intent = Intent(this, MainActivity::class.java)
//                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                            intent.addCategory(Intent.CATEGORY_DEFAULT)
//                            intent.data = Uri.parse("package:" + getPackageName())
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
//                            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
//                            ContextCompat.startActivity(this, intent, null)
//                        }).show()
//                }
//                return
//            }
//        }
//    }
//
//    companion object {
//        private const val TAG = "MainActivity"
//        private const val REQUEST_CODE = 1000
//        private const val DISPLAY_WIDTH = 720
//        private const val DISPLAY_HEIGHT = 1280
//        private val ORIENTATIONS = SparseIntArray()
//        private const val REQUEST_PERMISSIONS = 10
//
//        init {
//            ORIENTATIONS.append(Surface.ROTATION_0, 90)
//            ORIENTATIONS.append(Surface.ROTATION_90, 0)
//            ORIENTATIONS.append(Surface.ROTATION_180, 270)
//            ORIENTATIONS.append(Surface.ROTATION_270, 180)
//        }
//    }
//}

