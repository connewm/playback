package com.example.playback

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

//import kotlinx.android.synthetic.main.activity_main.*

import com.example.playback.ui.database_test.DBTestFragment
import com.example.playback.ui.database_test.DBTestViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_database.*
import kotlinx.android.synthetic.main.fragment_personal.*

import org.json.JSONArray
import java.io.IOException
import java.io.InputStream


import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.PlayerState

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.Track;

class MainActivity : AppCompatActivity() {
    lateinit var db : DBManager

    val TAG = "MainActivity"
    private val CLIENT_ID = "f4e7b6f3768a4e3ea9c44e4a5f1d8f9a"
    private val REDIRECT_URI = "com.example.playback://callback"
    private lateinit  var mSpotifyAppRemote: SpotifyAppRemote


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "called onCreate")

        /**
         * Before creating the view for the main activity we want to read from the provided JSON
         * file and store that data in the database for the user.
         * This process will mimic what our app will do every time the Main Activity is created
         * Which is call the Spotify API for the users top songs/artists/etc. and either create
         * a record for them, or update the current record so the user can view their personal
         * Spotify data in the Personal page
         *
         * Unfortunately we don't have the Spotify API 100% working yet, but the JSONs returned by
         * API calls will look exactly like the one stored in the res/raw directory now.
         */

        setContentView(R.layout.activity_main)


        db = DBManager(this)
        db.writableDatabase

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_maps,
                R.id.navigation_personal
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    // These 3 functions are not implemented yet because they depends on
    // which fragment will do these operations
    fun newData(v:View){

    }

    fun lookupData(v:View){

    }

    fun deleteData(v:View){

    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "called onStop")

        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "called onStart")



        // Set the connection parameters
        val connectionParams: ConnectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(this, connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    mSpotifyAppRemote = spotifyAppRemote
                    Log.d(TAG, "Connected! Yay!")
                    // Now you can start interacting with App Remote
                    connected()
                }

                override fun onFailure(throwable: Throwable) {
                    Log.e("MainActivity", throwable.message, throwable)
                    Log.e(TAG,"THIS IS CHEESE!!!")
                    // Something went wrong when attempting to connect! Handle errors here
                }
            })

    }


    private fun connected() { // Play a playlist
        //mSpotifyAppRemote.playerApi.play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL")
        //mSpotifyAppRemote.playerApi.play("spotify:track:3xgkq8uCVU2VGOHhdWtkCI")
        //mSpotifyAppRemote.playerApi.play("spotify:playlist:1FeniFvH4IcpLSPqTskJvF")
        // Subscribe to PlayerState

        mSpotifyAppRemote.playerApi
            .subscribeToPlayerState()
            .setEventCallback { playerState: PlayerState ->
                val track = playerState.track
                if (track != null) {
                    Log.d(TAG, track.name + " by " + track.artist.name)

                    //TODO get unique id
                    val userId = 0
                    val id = db.generate_record_id(userId)
                    val lat: Double = 27.2038
                    val long: Double = -77.5011


                    //TODO add data to the database
                    var newData = SpotifyPersonalData(id,userId, track.artist.name.toString(),
                        0,track.name.toString(), track.album.name.toString(),
                        "IDK YET", lat,long)

                    db.addData(newData)
                }
            }
    }


    override fun onResume() {
        super.onResume()
        Log.v(TAG, "called onResume")
    }

    override fun onPause() {
        Log.v(TAG, "calling onPause")
        super.onPause()
        Log.v(TAG, "called onPause")
    }

    override fun onRestart() {
        super.onRestart()
        Log.v(TAG, "called onRestart")
    }

    override fun onDestroy() {
        Log.v(TAG, "calling onDestroy")
        super.onDestroy()
    }


}