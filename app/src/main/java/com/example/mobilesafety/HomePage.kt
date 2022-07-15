package com.example.mobilesafety

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioRecord
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.system.Os.open
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.mobilesafety.R.layout.activity_home_page
import com.google.android.material.navigation.NavigationView
import java.nio.channels.AsynchronousFileChannel.open
import com.example.mobilesafety.R.layout.activity_main as activity_main

class HomePage : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var sos: Button
    private lateinit var video: Button
    private lateinit var find: Button
    private lateinit var liveStream: Button
    private lateinit var complaints: Button
    private lateinit var audioRec: Button

    private val REQUEST_CODE = 1

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_home_page)

        supportActionBar?.hide()

        sos = findViewById(R.id.sos)
        video = findViewById(R.id.video)
        find = findViewById(R.id.find)
        liveStream = findViewById(R.id.liveStream)
        complaints = findViewById(R.id.complaints)
        audioRec = findViewById(R.id.audioRec)

        video.setOnClickListener {

            video.isEnabled = openCameraToCaptureVideo()
            startActivity(intent)
        }

        find.setOnClickListener {
            val intent = Intent(this, GoogleMapCurrentLocation::class.java)
            startActivity(intent)
        }

        liveStream.setOnClickListener {
            val intent = Intent(this, LiveStream::class.java)
            startActivity(intent)
        }

        complaints.setOnClickListener {
            val intent = Intent(this, ComplaintActivity2::class.java)
            startActivity(intent)
        }

        audioRec.setOnClickListener {
            val intent = Intent(this, AudioRecorder::class.java)
            startActivity(intent)
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.navView)

        toggle = ActionBarDrawerToggle(this,drawerLayout, R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.nav_home -> {

                    Toast.makeText(applicationContext, "Clicked Home", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@HomePage, HomePage::class.java)
                    startActivity(intent)
                }
                R.id.nav_audioRec -> {

                    Toast.makeText(applicationContext, "Start Audio Recording", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@HomePage, AudioRecord::class.java)
                    startActivity(intent)
                }

                R.id.nav_callFamilyMember -> {

                    Toast.makeText(applicationContext, "Call any Family Member", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@HomePage, Settings::class.java)
                    startActivity(intent)
                }

                R.id.nav_video -> {

                    Toast.makeText(applicationContext, "Start Video recording", Toast.LENGTH_SHORT).show()
                    video.setOnClickListener {
                        video.isEnabled = openCameraToCaptureVideo()
                        startActivity(intent)
                    }
                }
                R.id.nav_liveStream -> {
                    Toast.makeText(applicationContext, "Start live streaming", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@HomePage, LiveStream::class.java)
                    startActivity(intent)
                }

                R.id.nav_myProfile -> {

                    Toast.makeText(applicationContext, "View/Edit My Profile", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@HomePage, AudioRecord::class.java)
                    startActivity(intent)
                }

                R.id.nav_complaints -> {

                    Toast.makeText(applicationContext, "Send us your complaints", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this@HomePage, ComplaintActivity2::class.java)
                    startActivity(intent)
                }
                R.id.nav_find -> {
                    Toast.makeText(applicationContext, "Clicked FInd Me", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@HomePage, GoogleMapCurrentLocation::class.java)
                    startActivity(intent)
                }

                R.id.nav_call -> {

                    Toast.makeText(applicationContext, "Call 911", Toast.LENGTH_SHORT).show()

                            val dialIntent = Intent(Intent.ACTION_DIAL)
                            dialIntent.data = Uri.parse("tel:" + "911")
                            startActivity(dialIntent)
                }
                R.id.nav_contact -> {
                    Toast.makeText(applicationContext, "Contact us today", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@HomePage, Contact::class.java)
                    startActivity(intent)
                }
                R.id.nav_logout -> {
                    Toast.makeText(applicationContext, "Logged Out", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@HomePage, Login::class.java)
                    startActivity(intent)
                    finish();
                }
            }
            true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)

    }

    private fun makePhoneCall(number: String): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_CALL)
            intent.setData(Uri.parse("tel:$number"))
            startActivity(intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    }

    private fun openCameraToCaptureVideo(): Boolean {
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) { // First check if camera is available in the device
            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            startActivityForResult(intent, REQUEST_CODE)
        }
        return true
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            if (intent?.data != null) {
                val uriPathHelper: Uri = intent.data!!
                val videoFullPath = uriPathHelper.getPath(

                ) // Use this video path according to your logic
                // if you want to play video just after recording it to check is it working (optional)
                if (videoFullPath != null) {
                    playVideoInDevicePlayer(videoFullPath)
                }
            }
        }
    }

    private fun playVideoInDevicePlayer(videoFullPath: String) {

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoFullPath))
        intent.setDataAndType(Uri.parse(videoFullPath), "video/mp4")
        startActivity(intent)
    }

    fun panic(view: View) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:" + "911")
        startActivity(dialIntent)
    }
}
