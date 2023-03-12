//package com.example.canvasrecorderwithffmpeg
//
//import android.app.Service
//import android.content.Context
//import android.content.Intent
//import android.content.res.Resources
//import android.hardware.display.DisplayManager
//import android.hardware.display.VirtualDisplay
//import android.media.MediaCodec
//import android.media.MediaCodecInfo
//import android.media.MediaFormat
//import android.media.MediaMuxer
//import android.media.projection.MediaProjection
//import android.media.projection.MediaProjectionManager
//import android.net.Uri
//import android.os.Environment
//import android.os.Handler
//import android.os.HandlerThread
//import android.os.IBinder
//import android.os.Looper
//import android.os.Message
//import android.util.DisplayMetrics
//import android.view.Surface
//import java.io.IOException
//import java.util.Base64.Encoder
//import kotlin.math.pow
//
//class ScreenCaptureService: Service() {
//	private var  handler: ServiceHandler? = null
//	private var pData: Intent? = null
//
//	init {
//		val thread = HandlerThread("handler thread")
//		thread.start()
//
//		val p = (getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager)
//			.getMediaProjection(-1, pData!!)
//
//		handler = ServiceHandler(thread.looper, p)
//	}
//
//	private inner class ServiceHandler(looper: Looper, private val mediaProj: MediaProjection) : Handler(looper) {
//		private var encoder: MediaCodec? = null
//		private var muxer: MediaMuxer? = null
//		private val mimetype = MediaFormat.MIMETYPE_VIDEO_MPEG4
//		private val videoUrl: String = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)}/VIDEO_RECORDER.mp4"
//
////		private val videoUrl: String = Uri.parse("https://kenangan.s3-ap-southeast-1.amazonaws.com/content-cache/video/43531af3-4e31-42dd-b40c-0e18901e892b.mp4")
//		private val frameRate = 30
//		private val bitRate: Int = 6 * 2.0.pow(20).toInt() * 8
//		private val keyFrameFreq = 2;
//		private val colorFormat = MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface
//		private var virtualDisplay: VirtualDisplay? = null;
////		private var mediaProj: MediaProjection? = null
//
//
//
//		override fun handleMessage(msg: Message) {
//			when(msg.what) {
//				0 -> {
//					// create the encoder and muxer object
//					try {
//							encoder = MediaCodec.createEncoderByType(mimetype)
//							muxer = MediaMuxer(videoUrl, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
//					} catch (e: IOException) {
//							e.printStackTrace()
//					}
//
//					// set callback to run the encoder in async mode
//					encoder?.setCallback(object: MediaCodec.Callback() {
//						private val TAG: String = "encoder_callback"
//						var track: Int? = null;
//						var initialPts:  Long = 0
//
//						override fun onInputBufferAvailable(codec: MediaCodec, index: Int) {
//							// handle input buffer available event
//						}
//
//						override fun onOutputBufferAvailable(codec: MediaCodec, index: Int, info: MediaCodec.BufferInfo) {
//							// if we encounter an end of the stream buffer
//							if (info.flags == MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
//								muxer!!.writeSampleData(track!!, codec!!.getOutputBuffer(index)!!, info)
//
//								//release the muxer
//								muxer!!.stop()
//								muxer!!.release()
//
//
//								//relase the encoder
//								encoder!!.stop()
//								encoder!!.release()123
//								return;
//							}
//
//							if (info.presentationTimeUs != 0) {
//								if (initialPts == 0) {
//									initialPts =  info.presentationTimeUs
//
//								} else {
//									info.presentationTimeUs -= initialPts
//								}
//							}
//						}
//
//						override fun onOutputFormatChanged(codec: MediaCodec, format: MediaFormat) {
//							// handle output format changed event
//							track = muxer!!.addTrack(format)
//							muxer!!.start()
//						}
//
//						override fun onError(codec: MediaCodec, e: MediaCodec.CodecException) {
//							// handle error event
//						}
//					})
//
//					// config the encoder
//					val m: DisplayMetrics = Resources.getSystem().displayMetrics
//					val videoFormat: MediaFormat = MediaFormat.createVideoFormat(mimetype, m.widthPixels, m.heightPixels)
//					videoFormat.setInteger(MediaFormat.KEY_FRAME_RATE, keyFrameFreq)
//					videoFormat.setInteger(MediaFormat.KEY_BIT_RATE, bitRate)
//					videoFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, keyFrameFreq)
//					videoFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, colorFormat)
//
//					encoder?.configure(videoFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
//					val surface: Surface = encoder!!.createInputSurface()
//
//					encoder!!.start()
//
//					virtualDisplay = mediaProj?.createVirtualDisplay(
//						"virtual display",
//						m.widthPixels,
//						m.heightPixels,
//						m.densityDpi,
//						DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
//						surface,
//						null,
//						null
//					)
//				}
//
//				// stop screen capturing
//				1 -> {
//
//				}
//			}
//		}
//	}
//
//
//	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//		super.onStartCommand(intent, flags, startId)
//
//		val action = intent!!.action
//
//		when(action) {
//			"start"-> {
//				pData = intent.getParcelableExtra("com.ns.pData")
//				val m: Message = handler!!.obtainMessage()
//				m.what = 0
//				handler!!.sendMessage(m)
//			}
//			"stop"-> {
//				val m: Message = handler!!.obtainMessage()
//				m.what = 1
//				handler!!.sendMessage(m)
//			}
//		}
//
//		return START_NOT_STICKY
//	}
//
//	override fun onBind(intent: Intent?): IBinder? {
//		return null
//	}
//}