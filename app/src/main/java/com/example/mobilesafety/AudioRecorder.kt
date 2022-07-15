package com.example.mobilesafety

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import androidx.core.app.ActivityCompat

class AudioRecorder : AppCompatActivity() {

    lateinit var mr : MediaRecorder

    private lateinit var playButton: Button
    private lateinit var stopButton: Button
    private lateinit var btnCancel3: Button
    private lateinit var recordButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_recorder)

        supportActionBar?.hide()

        btnCancel3 = findViewById(R.id.btnCancel3)
        playButton = findViewById(R.id.playButton)
        stopButton = findViewById(R.id.stopButton)
        recordButton = findViewById(R.id.recordButton)

        btnCancel3.setOnClickListener {
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }

        var path = Environment.getExternalStorageDirectory().toString()+"/myrec.3gp"
        mr = MediaRecorder()

        playButton.isEnabled = false
        stopButton.isEnabled = false

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE), 111)
        playButton.isEnabled = true
        playButton.setOnClickListener {

        mr.setAudioSource(MediaRecorder.AudioSource.MIC)
            mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mr.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB)
            mr.setOutputFile(path)
            mr.prepare()
            mr.start()
            stopButton.isEnabled = true
            playButton.isEnabled = false
        }
        stopButton.setOnClickListener {
            mr.stop()
            playButton.isEnabled = true
            stopButton.isEnabled = false
        }
       recordButton.setOnClickListener {
           var mp = MediaPlayer()
           mp.setDataSource(path)
           mp.prepare()
           mp.start()
       }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED)

            playButton.isEnabled = true
    }
}