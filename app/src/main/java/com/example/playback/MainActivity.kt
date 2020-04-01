package com.example.playback

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import org.json.JSONArray
import java.io.IOException
import java.io.InputStream
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.PlayerState


class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"
    private val CLIENT_ID = "f4e7b6f3768a4e3ea9c44e4a5f1d8f9a"
    private val REDIRECT_URI = "com.example.playback://callback"
    private lateinit  var mSpotifyAppRemote: SpotifyAppRemote


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.v(TAG,"called onCreate")

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
        add_records()

        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_social, R.id.navigation_maps, R.id.navigation_personal
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // show added records in list view in personal page
        show_records()
    }

    // read the underlying JSON that represents a Spotify callback
    fun read_json(): JSONArray
    {
        var json: String? = null

        try {
            val inputStream:InputStream = assets.open("test.json")
            json = inputStream.bufferedReader().use { it.readText() }

            var json_arr = JSONArray(json)

            return json_arr
            /*for (i in 0..json_arr.length())
            {
                var json_object = json_arr.getJSONObject(i)
                json_object.getString("key")
            }*/
        } catch (e: IOException)
        {

        }

    }

    // add records if they don't already exist
    // TODO: create check so were not adding records no matter what
    // Add no matter what for now (e.g. do no update)
    fun add_records()
    {
        // db helper instance variable for calling CRUD operations
        var sbdh = SpotPersonalDBHelper()
        var json_arr = read_json()
        // decalare the variables to be stored in the db from JSON file
        var record_id = 0 // increment in each pass
        var user_id = 0 // TODO: hard coded, for now, eventually get user id on each sessaion and use for data
        var artist: String
        var popularity: Int
        for (idx in 0..json_arr.length())
        {
            var json_object = json_arr.getJSONObject(idx)
            artist = json_object.getString("name")
            popularity = json_object.getInt("popularity")
            sbdh.insert_personal_record(SpotPersonalData(record_id, user_id, artist, popularity))
        }

    }

    // dummy function to show pulled up records in main fragment text view
    //TODO: IMplement this function. It should show the retrieved records after the create/insert operation
    // TODO: In a listview in the personal page. Use SpotPersonalDBHelper.read_user_personal_data(users_id)
    fun show_records()
    {
        //
    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG,"called onStop")
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG,"called onStart")
        // Set the connection parameters
        // Set the connection parameters
        val connectionParams: ConnectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(this, connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    mSpotifyAppRemote = spotifyAppRemote
                    Log.d(TAG, "SPOTIFY CONNECTED!")
                    // Now you can start interacting with App Remote
                    connected()
                }

                override fun onFailure(throwable: Throwable) {
                    Log.e("MainActivity", throwable.message, throwable)
                    Log.e(TAG,"SPOTIFY DIDNT CONNECT!!!")
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
                }
            }
    }

    override fun onResume() {
        super.onResume()
        Log.v(TAG,"called onResume")
    }

    override fun onPause() {
        Log.v(TAG,"calling onPause")
        super.onPause()
        Log.v(TAG,"called onPause")
    }

    override fun onRestart() {
        super.onRestart()
        Log.v(TAG,"called onRestart")
    }

    override fun onDestroy() {
        Log.v(TAG,"calling onDestroy")
        super.onDestroy()
    }


}
