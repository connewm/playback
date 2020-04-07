package com.example.playback

import android.app.WallpaperColors.fromBitmap
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.IDNA
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_database.*
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var db : DBManager

    private var mLocationRequest: LocationRequest? = null
    private val UPDATE_INTERVAL = (10*1000).toLong()
    private val FASTEST_INTERVAL: Long = 2000

    private var latitude = 0.0
    private var longitude = 0.0

    private lateinit var lastLocation: Location

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        Log.w("map on Create", "async is about to be called")
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        try {
            db = DBManager(this.applicationContext)
            Log.w("asdf", "connection successful")
        } catch(e: Exception)
        {
            Log.w("asdf", "connection unsuccessful")
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        Log.w("asdf", "onMapReady is called")

        mMap = googleMap

        mMap.getUiSettings().setZoomControlsEnabled(true)
        mMap.setOnMarkerClickListener(this)
        setUpMap()


    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) {

            if (it != null)
            {
                lastLocation = it
                val currentLatLng = LatLng(it.latitude, it.longitude)
                placeMarkerOnMap()
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17f))
            }
        }
    }

    // TODO: only one pin for now, eventually will have many
    // TODO: look into info window for custom markers
    // TODO: custom marker icon
    // TODO: retrieve album cover and set icon property
    private fun placeMarkerOnMap()
    {

        val arr = db.findData(0)
        var differential: Double = 0.0
        var prev_entry_song: String = ""
        for (first_entry in arr) {
            if (!first_entry.songName.equals(prev_entry_song)) {
                val marker_location: LatLng =
                    LatLng(
                        first_entry.listenLocationLatitude - differential,
                        first_entry.listenLocationLongitude
                    )

                val markerOptions =
                    MarkerOptions().position(marker_location).title("${first_entry.songName}")
                        .snippet("By: ${first_entry.artistName}")
                mMap.addMarker(markerOptions).showInfoWindow()
                differential -= 0.001
                prev_entry_song = first_entry.songName.toString()
            }
        }
    }

    override fun onMarkerClick(p0: Marker?) = false
}
