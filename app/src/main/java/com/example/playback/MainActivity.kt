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
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess



class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"

    //creating an object that handles database operations
    lateinit var db : DBManager

    //create an object taht will handle spotify operations
    var sc : SpotifyConnector = SpotifyConnector()

    //LIFE CYCLE METHODS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
<<<<<<< HEAD
        Log.d(TAG, "called onCreate")
=======
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
>>>>>>> chartInPersonal


        //initialize the dataBase
        initializeDataBaseConnection()

        //Try to connect to Spotify which initializes mSpotifyAppRemote
        sc.connectToSpotify(this.applicationContext)


        Log.d(TAG, "spotify connected")

        sc.connectDataBase(db)

        //Setting up activity UI
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
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


    override fun onStart() {
        super.onStart()
        Log.v(TAG, "called onStart")
        if(!sc.isConnected()) sc.connectToSpotify(this.applicationContext)

    }

<<<<<<< HEAD
=======

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

                    val id = db.generate_record_id(0) // db.generate_record_id(userId)
                    Log.w("asdf", "$id")

                    val lat: Double = 39.9805
                    val long: Double = -83.0038

                    //TODO add data to the database
                    var newData = SpotifyPersonalData(id,userId, track.artist.name.toString(),
                        0,track.name.toString(), track.album.name.toString(),
                        "IDK YET", lat,long)
                    var response: Boolean = false

                    response = db.addData(newData)



                    if (response) {
                        Log.w("asdf", "add operation worked")
                    } else {
                        Log.w("asdf", "addData returned false")
                    }
                }
            }
    }


>>>>>>> chartInPersonal
    override fun onResume() {
        super.onResume()
        Log.v(TAG, "called onResume")
        if(!sc.isConnected()) sc.connectToSpotify(this.applicationContext)
    }

    override fun onPause() {
        Log.v(TAG, "calling onPause")
        super.onPause()
        Log.v(TAG, "called onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "called onStop")

        //disconnect to Spotify
        sc.disconnectToSpotify()
    }

    override fun onRestart() {
        super.onRestart()
        Log.v(TAG, "called onRestart")
    }

    override fun onDestroy() {
        Log.v(TAG, "calling onDestroy")
        super.onDestroy()
    }



    //Spotify connection functions
    fun connectToSpotify(){

    }





    //Database functions
    fun initializeDataBaseConnection(){
        val tempTag = "DataBaseConection"
        try {
            db = DBManager(this.applicationContext)
            Log.w(tempTag, "connection successful")
        } catch(e:Exception)
        {
            Log.w(tempTag, "connection unsuccessful")
        }
    }

    // These 3 functions are not implemented yet because they depends on
    // which fragment will do these operations
    fun newData(v:View){

    }

    fun lookupData(v:View){

    }

    fun deleteData(v:View){

    }
}