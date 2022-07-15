package com.example.mobilesafety

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
//import com.example.mapapp.databinding.ActivityMapsBinding
import com.example.mobilesafety.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    @IgnoreExtraProperties
    data class LocationInfo(
        var latitude: Double? = 0.0,
        var longitude: Double? = 0.0
    )
    private lateinit var binding: ActivityMapsBinding
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var currentLocation: Location?= null
    private lateinit var map: GoogleMap
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var dbReference: DatabaseReference = database.getReference("test")
    private lateinit var find_location_btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        supportActionBar?.hide()

        find_location_btn = findViewById(R.id.btn_find_location)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        // Get a reference from the database so that the app can read and write operations
        dbReference = Firebase.database.reference
        dbReference.addValueEventListener(locListener)
    }

    val locListener = object : ValueEventListener {
        //     @SuppressLint("LongLogTag")
        override fun onDataChange(snapshot: DataSnapshot) {
            if(snapshot.exists()){
                //get the exact longitude and latitude from the database "test"
                val location = snapshot.child("test").getValue(LocationInfo::class.java)
                val locationLat = location?.latitude
                val locationLong = location?.longitude
                //trigger reading of location from database using the button
                find_location_btn.setOnClickListener {

                    // check if the latitude and longitude is not null
                    if (locationLat != null && locationLong!= null) {
                        // create a LatLng object from location
                        val latLng = LatLng(locationLat, locationLong)
                        //create a marker at the read location and display it on the map
                        map.addMarker(MarkerOptions().position(latLng)
                            .title("The user is currently here"))
                        //specify how the map camera is updated
                        val update = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f)
                        //update the camera with the CameraUpdate object
                        map.moveCamera(update)
                        startActivity(intent)
                    }
                    else {
                        // if location is null , log an error message
                        Log.e(TAG, "user location cannot be found")
                    }
                }

            }
        }
        // show this toast if there is an error while reading from the database
        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(applicationContext, "Could not read from database", Toast.LENGTH_LONG).show()
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap //initialize map when the map is ready
        startActivity(intent)
    }
    companion object {
        // TAG is passed into the Log.e methods used above to print information to the Logcat window
        private const val TAG = "MapsActivity" // for debugging
    }
}


