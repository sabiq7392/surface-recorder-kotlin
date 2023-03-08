package com.example.canvasrecorderwithffmpeg

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private const val LOG_TAG = "AudioRecordTest"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class MainActivity : AppCompatActivity(), SurfaceHolder.Callback {

    private lateinit var surfaceView: SurfaceView
    private lateinit var surfaceHolder: SurfaceHolder
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var startRecording: Button
    private lateinit var stopRecording: Button
    private lateinit var replayVideo: Button
    private var mediaRecorder: MediaRecorder? = null

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
//            grantResults[0] == PackageManager.PERMISSION_GRANTED
//        } else {
//            false
//        }
//        if (!permissionToRecordAccepted) finish()
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startRecording = findViewById(R.id.start_recording)
        stopRecording = findViewById(R.id.stop_recording)
        stopRecording.setEnabled(false)
        replayVideo = findViewById(R.id.replay_video)

        surfaceView = findViewById(R.id.surface_view)
        surfaceHolder = surfaceView.holder
        surfaceHolder.addCallback(this)

        mediaRecorder = MediaRecorder()

    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDisplay(surfaceHolder)

        val videoUri = Uri.parse("https://kenangan.s3-ap-southeast-1.amazonaws.com/content-cache/video/43531af3-4e31-42dd-b40c-0e18901e892b.mp4")
        mediaPlayer.setDataSource(this, videoUri)
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
            replayVideo.setEnabled(false)
            println("starting video....")
        }
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnCompletionListener {
            replayVideo.setEnabled(true)
            println("stop  video...")
        }


        replayVideo.setOnClickListener() {
            mediaPlayer.start()
            replayVideo.setEnabled(false)
            println("replay video....")
        }

        startRecording.setOnClickListener {
            stopRecording.setEnabled(true)
//            // Konfigurasi Media Recorder
//            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE)
//            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
//            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT)
////            mediaRecorder.setVideoSize(surfaceView.width, surfaceView.height)
//            mediaRecorder.setOutputFile(getOutputMediaFile().toString())
//            mediaRecorder.setMaxDuration(5000)
//            mediaRecorder.setPreviewDisplay(surfaceHolder.surface)
//            mediaRecorder.prepare()

            // Mulai merekam video
//            mediaRecorder.start()
            // Konfigurasi media recorder untuk merekam video
//            mediaRecorder?.apply {
//                setVideoSource(MediaRecorder.VideoSource.SURFACE)
//                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
//                setOutputFile(getOutputMediaFile().toString())
//                setVideoEncodingBitRate(10000000)
//                setVideoEncoder(MediaRecorder.VideoEncoder.H264) // Atur encoder video
//                setVideoFrameRate(30)
//                setMaxDuration((5000))
//                setOnInfoListener { mr, what, extra ->
//                    // Callback ketika media recorder memberikan informasi
//                    when (what) {
//                        MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED -> {
//                            // Tindakan yang diambil ketika durasi maksimal tercapai
//                            mediaRecorder?.stop()
//                            mediaRecorder?.release()
//                            stopRecording.setEnabled(false)
//                            println("stop recording...")
//                        }
//                    }
//                }
//                prepare()
//                start()
//            }

            mediaPlayer.seekTo(0)
            startRecording.setEnabled(false)


            println("start recording..." )
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        println("surface changed...")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // Stop and release MediaRecorder
        mediaRecorder?.stop()
        mediaRecorder?.release()

        // Stop and release MediaPlayer
        mediaPlayer.stop()
        mediaPlayer.release()
        println("surface destroyed...")
    }

    private fun getOutputMediaFile(): File? {
        // Create a media file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

        println(Environment.DIRECTORY_DCIM)
        println("get out put media file ...")

        return File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
            "VID_$timeStamp.mp4"
        )
    }

}

