package com.example.canvasrecorderwithffmpeg

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaFormat
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

private const val LOG_TAG = "AudioRecordTest"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class MainActivity : AppCompatActivity(), SurfaceHolder.Callback {
//    private var camera: Camera? = null
    private var surfaceView: SurfaceView? = null
    private var surfaceHolder: SurfaceHolder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var buttonStartRecording: Button? = null
    private var buttonStopRecording: Button? = null
    private var mediaRecorder: MediaRecorder? = null
    private var permissionToRecordAccepted = false
    private val PERMISSIONS_REQUEST_CAMERA = 123
//    private var mediaCodec: MediaCodec? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonStartRecording = findViewById(R.id.start_recording)
        buttonStartRecording?.isEnabled = false

        buttonStopRecording = findViewById(R.id.stop_recording)
        buttonStopRecording?.isEnabled = false

//        mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_MPEG4)
        surfaceView = findViewById(R.id.surface_view)
        surfaceHolder = surfaceView?.holder
        surfaceHolder?.addCallback(this)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun surfaceCreated(holder: SurfaceHolder) {
        val videoUri = Uri.parse("https://kenangan.s3-ap-southeast-1.amazonaws.com/content-cache/video/43531af3-4e31-42dd-b40c-0e18901e892b.mp4")

        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDisplay(holder)
        mediaPlayer?.setDataSource(this, videoUri)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            println("video is prepared...")
            buttonStartRecording?.isEnabled = true
        }

        buttonStartRecording?.setOnClickListener {
            if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA), 1)
            } else {
                println("start recording...")
                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

                mediaPlayer?.start()

                mediaRecorder = MediaRecorder()
                mediaRecorder?.apply {
                    setVideoSource(MediaRecorder.VideoSource.SURFACE)
                    setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    setOutputFile("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)}/recording_${timeStamp}.mp4")
                    setVideoEncoder(MediaRecorder.VideoEncoder.H264)
                    setVideoEncodingBitRate(10000000)
                    setVideoFrameRate(30)
                    setVideoSize(640, 480)
//                    setInputSurface(holder.surface)
                    setOnErrorListener { mr, what, extra ->
                        println("media recorder is error...")
                        println(mr)
                        println(what)
                        println(extra)
                    }
                    setOnInfoListener { mr, what, extra ->
                        println("info for media recorder...")
                        println(mr)
                        println(what)
                        println(extra)
                    }
                    setPreviewDisplay(surfaceView?.holder?.surface)
                    prepare()
                    start()
                }

                buttonStopRecording?.setEnabled(true)
                buttonStartRecording?.setEnabled(false)
            }
        }

        buttonStopRecording?.setOnClickListener() {
            onStopRecording()
        }

        mediaPlayer?.setOnCompletionListener {
            onStopRecording()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        println("surface changed...")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        println("surface destroyed...")
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaPlayer?.apply {
            stop()
            release()
        }
    }

    private fun onStopRecording() {
        println("stop recording...")

        try {
            mediaRecorder?.apply {
                stop()
                reset()
                release()
            }
            mediaRecorder = null
            buttonStopRecording?.isEnabled = false
            buttonStartRecording?.isEnabled = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun onStartRecording(holder: SurfaceHolder) {
        println("start recording...")
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

        mediaRecorder?.apply {
            setVideoSource(MediaRecorder.VideoSource.SURFACE)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)}/recording_${timeStamp}.mp4")
            setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            setVideoEncodingBitRate(10000000)
            setVideoSize(surfaceView!!.width, surfaceView!!.height)
            setVideoFrameRate(30)
//            setInputSurface(holder.surface)
            prepare()
            setOnErrorListener { mr, what, extra ->
                println("media recorder is error...")
                println(mr)
                println(what)
                println(extra)
            }
            setOnInfoListener { mr, what, extra ->
                println("info for media recorder...")
                println(mr)
                println(what)
                println(extra)
            }
        }
    }

    private fun getOutputMediaFile(): File? {
        println("get out put media file ...")

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

        println(Environment.DIRECTORY_DCIM)

        return File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
            "VID_$timeStamp.mp4"
        )
    }
}

