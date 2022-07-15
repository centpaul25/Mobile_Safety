package com.example.mobilesafety

import android.graphics.Point
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar

class LiveStream : AppCompatActivity(), SurfaceHolder.Callback {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var surfaceView: SurfaceView
    private lateinit var progressBar: ProgressBar
    private lateinit var surfaceContainer: FrameLayout
    private var playbackPosition = 0
    private val rtspUrl = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_175k.mov"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_stream)

        supportActionBar?.hide()

        surfaceView = findViewById(R.id.surfaceView)
        progressBar = findViewById(R.id.progressBar)
        surfaceContainer = findViewById(R.id.surfaceContainer)

        val holder = surfaceView.holder
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        val surface = holder.surface
        setupMediaPlayer(surface)
        prepareMediaPlayer()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {

    }

    override fun onPause() {
        super.onPause()
        playbackPosition = mediaPlayer.currentPosition
    }

    override fun onStop() {
        mediaPlayer.stop()
        mediaPlayer.release()
        super.onStop()
    }

    private fun createAudioAttributes(): AudioAttributes{
        val builder = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
        return builder.build()
    }

    private fun setupMediaPlayer(surface: Surface){
        progressBar.visibility = View.VISIBLE
        mediaPlayer = MediaPlayer()
        mediaPlayer.setSurface(surface)
        val audioAttributes = createAudioAttributes()
        mediaPlayer.setAudioAttributes(audioAttributes)
        val uri = Uri.parse(rtspUrl)
        try {
            mediaPlayer.setDataSource(this, uri)
        } catch (e:IllegalArgumentException){
            e.printStackTrace()
        }

    }
private fun setSurfaceDimensions(player: MediaPlayer, width: Int, height: Int){
    if (width > 0 && height > 0){
        val aspectRatio = height.toFloat() / width.toFloat()
        val screenDimensions = Point()
        windowManager.defaultDisplay.getSize(screenDimensions)
        val surfaceWidth = screenDimensions.x
        val surfaceHeight = (surfaceWidth * aspectRatio).toInt()
        val params = FrameLayout.LayoutParams(surfaceWidth, surfaceHeight)
        surfaceView.layoutParams = params
        val holder = surfaceView.holder
        player.setDisplay(holder)
    }
}

    private fun prepareMediaPlayer(){
        try {
            mediaPlayer.prepareAsync()
        } catch (e:IllegalArgumentException){
            e.printStackTrace()
        }
        mediaPlayer.setOnPreparedListener {
            progressBar.visibility = View.INVISIBLE
            mediaPlayer.seekTo(playbackPosition)
            mediaPlayer.start()
        }

        mediaPlayer.setOnVideoSizeChangedListener { player, width, height ->

            setSurfaceDimensions(player, width, height)
        }

    }
}