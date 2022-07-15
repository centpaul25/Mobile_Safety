package com.example.mobilesafety

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.VideoView

private lateinit var VideoView: VideoView
private lateinit var Button: Button
private lateinit var EditText: EditText
private lateinit var ProgressBar: ProgressBar



class VideoUpload : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_upload)
    }

    fun ChooseVideo(view: View) {

    }

    fun ShowVideo(view: View) {}
}