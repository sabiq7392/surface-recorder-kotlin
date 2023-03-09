package com.example.canvasrecorderwithffmpeg

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private const val LOG_TAG = "AudioRecordTest"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class MainActivity : AppCompatActivity(), SurfaceHolder.Callback {

    private var surfaceView: SurfaceView? = null
    private var surfaceHolder: SurfaceHolder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var buttonStartRecording: Button? = null
    private var buttonStopRecording: Button? = null
    private var buttonReplayVideo: Button? = null
    private var mediaRecorder: MediaRecorder? = null
    private var permissionToRecordAccepted = false
    private val PERMISSIONS_REQUEST_CAMERA = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonStartRecording = findViewById(R.id.start_recording)
        buttonStopRecording = findViewById(R.id.stop_recording)
        buttonStopRecording?.setEnabled(false)
        buttonReplayVideo = findViewById(R.id.replay_video)

        surfaceView = findViewById(R.id.surface_view)
        surfaceHolder = surfaceView?.holder
        surfaceHolder?.addCallback(this)


        buttonStartRecording?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 1)
            } else {
                onStartRecording()
                mediaPlayer?.setOnCompletionListener {
                    onStopRecording()
                }
            }
        }

        buttonStopRecording?.setOnClickListener() {
            onStopRecording()
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDisplay(surfaceHolder)

        val videoUri = Uri.parse("https://kenangan.s3-ap-southeast-1.amazonaws.com/content-cache/video/43531af3-4e31-42dd-b40c-0e18901e892b.mp4")
        mediaPlayer?.setDataSource(this, videoUri)
        mediaPlayer?.setOnPreparedListener {
            println("starting video....")
            mediaPlayer?.start()
            buttonReplayVideo?.setEnabled(false)
        }
        mediaPlayer?.prepareAsync()
        buttonReplayVideo?.setOnClickListener() {
            println("replay video....")
            mediaPlayer?.start()
            buttonReplayVideo?.setEnabled(false)
        }
//
//        buttonStopRecording?.setOnClickListener() {
//            mediaRecorder?.stop()
//            mediaRecorder?.release()
//        }
//
//
//        buttonStartRecording?.setOnClickListener {
//            println("start recording..." )
//            if (ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.CAMERA
//                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.RECORD_AUDIO
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),
//                    PERMISSIONS_REQUEST_CAMERA
//                )
//            }
////   else {
//                buttonStartRecording?.setEnabled(false);
//                buttonStopRecording?.setEnabled(true)
//                mediaRecorder?.setVideoSource(MediaRecorder.VideoSource.SURFACE)
//                mediaRecorder?.setPreviewDisplay(surfaceView?.getHolder()?.getSurface());
//                mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
//                mediaRecorder?.setVideoFrameRate(60)
////                mediaRecorder?.setVideoSize(surfaceView!!.getWidth(), surfaceView!!.getHeight())
//                mediaRecorder?.setOutputFile(getOutputMediaFile())
//                mediaRecorder?.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT)
//                mediaRecorder?.setMaxDuration(5000)
//                mediaRecorder?.prepare()
//
//                mediaRecorder?.start()
////            }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        println("surface changed...")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        println("surface destroyed...")

        // Stop and release MediaRecorder
        mediaRecorder?.stop()
        mediaRecorder?.release()

        // Stop and release MediaPlayer
        mediaPlayer?.stop()
        mediaPlayer?.release()
    }

    private fun onStopRecording() {
        println("stop recording...")
        mediaRecorder?.stop()
        mediaRecorder?.release()
        buttonStopRecording?.setEnabled(false)
        buttonStartRecording?.setEnabled(true)
    }

    private fun onStartRecording() {
        println("start recording...")
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        mediaRecorder = MediaRecorder()
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder?.setOutputFile("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)}/recording_${timeStamp}.3gp")
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mediaRecorder?.prepare()
        mediaRecorder?.start()
        buttonStopRecording?.setEnabled(true)
        buttonStartRecording?.setEnabled(false)
    }
//    private fun getOutputMediaFile(): File? {
//        println("get out put media file ...")
//
//        // Create a media file name
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//
//        println(Environment.DIRECTORY_DCIM)
//
//        return File(
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
//            "VID_$timeStamp.mp4"
////            "VID_TESTER_RECORDER.mp4"
//        )
//    }

}

