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



class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"

    //creating an object that handles database operations
    lateinit var db : DBManager

    //create an object taht will handle spotify operations
    lateinit var sc : SpotifyConnector

    //LIFE CYCLE METHODS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "called onCreate")


        //initialize the dataBase
        initializeDataBaseConnection()

        //Try to connect to Spotify which initializes mSpotifyAppRemote

        sc = SpotifyConnector()
        sc.connectToSpotify(this.applicationContext)

        while(sc.notConnected()) {
            Thread.sleep(1_000)
        }

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