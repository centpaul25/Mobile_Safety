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
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat.startActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import java.nio.channels.AsynchronousFileChannel.open
import com.example.mobilesafety.R.layout.activity_main as activity_main

class MainActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var sos: Button
    private lateinit var video: Button
    private lateinit var find: ImageButton
    private val REQUEST_CODE = 1

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)

        sos = findViewById(R.id.sos)
        video = findViewById(R.id.video)
        find = findViewById(R.id.find)
        video.isEnabled = openCameraToCaptureVideo()

        find.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        sos.setOnClickListener {

            makePhoneCall("911")
            startActivity(intent)
        }
        navView.setNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.nav_home -> {

                    Toast.makeText(applicationContext, "Clicked Home", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_audioRec -> {

                    Toast.makeText(applicationContext, "Clicked Audio Recorder", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, AudioRecord::class.java)
                    startActivity(intent)
                }

                R.id.nav_callFamilyMember -> {

                    Toast.makeText(applicationContext, "Call Family Member", Toast.LENGTH_SHORT).show()
                    val dialIntent = Intent(Intent.ACTION_DIAL)
                    dialIntent.data = Uri.parse("tel:" + "07864708193")
                    startActivity(dialIntent)

                }

                R.id.nav_complaints -> {

                    Toast.makeText(applicationContext, "Clicked Complaint", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this@MainActivity, ComplaintActivity2::class.java)
                    startActivity(intent)
                }
                R.id.nav_find -> {
                    Toast.makeText(applicationContext, "Clicked Map", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, MapsActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_contact -> {
                    Toast.makeText(applicationContext, "Contact Us", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, Contact::class.java)
                    startActivity(intent)
                }
                R.id.nav_logout -> {
                    Toast.makeText(applicationContext, "Logged Out", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, Login::class.java)
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
}
