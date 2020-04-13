package com.example.playback



import android.content.Context
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.PlayerState

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.Track;
import java.lang.Exception

class SpotifyConnector {
    //Spotify variables
    private val CLIENT_ID = "f4e7b6f3768a4e3ea9c44e4a5f1d8f9a"
    private val REDIRECT_URI = "com.example.playback://callback"

    companion object  {
        private lateinit  var mSpotifyAppRemote: SpotifyAppRemote
        private  lateinit var db : DBManager
    }

    fun connectToSpotify(c: Context){
        //Connect to Spotify
        // Set the connection parameters
        val connectionParams: ConnectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect( c, connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    mSpotifyAppRemote = spotifyAppRemote
                    Log.d(TAG, "Connected! Yay!")
                    // Now you can start interacting with App Remote
                    continuoslyAddSongsToDataBase()
                }

                override fun onFailure(throwable: Throwable) {
                    Log.e("MainActivity", throwable.message, throwable)
                    Log.e(TAG,"THIS IS CHEESE!!!")
                    // Something went wrong when attempting to connect! Handle errors here
                }
            })
    }



    fun disconnectToSpotify(){
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }



    private fun continuoslyAddSongsToDataBase() {


        // Subscribe to PlayerState
        mSpotifyAppRemote.playerApi
            .subscribeToPlayerState()
            .setEventCallback { playerState: PlayerState ->
                val track = playerState.track
                if (track != null) {
                    Log.d(TAG, track.name + " by " + track.artist.name)

                    //TODO get unique id
                    val userId = 0

                    val id = db.generate_record_id(userId) // db.generate_record_id(userId)
                    Log.w("asdf", "$id")

                    val lat: Double = 39.9805
                    val long: Double = -83.0038

                    //TODO add data to the database
                    var newData = SpotifyPersonalData(id,userId, track.artist.name.toString(),
                        0,track.name.toString(), track.album.name.toString(),
                        "IDK YET", lat,long)
                    var response: Boolean = false
                    try {
                        response = db.addData(newData)
                    } catch(e:Exception) {
                        Log.w("asdf", "add was unsuccessful")
                    }

                    if (response) {
                        Log.w("asdf", "add operation worked")
                    }
                }
            }
    }


    fun connectDataBase(dataBase: DBManager){
        db = dataBase
    }


    fun playSong(s:String){
        // Play a playlist
        //mSpotifyAppRemote.playerApi.play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL")
        //mSpotifyAppRemote.playerApi.play("spotify:track:3xgkq8uCVU2VGOHhdWtkCI")
        //mSpotifyAppRemote.playerApi.play("spotify:playlist:1FeniFvH4IcpLSPqTskJvF")
        mSpotifyAppRemote.playerApi.play(s)
    }

    fun getRecentlyPlayed(): Array<Pair<String?, String?>> {

        var dataSet: Array<Pair<String?, String?>> =  arrayOf()

        //obtain data from the database
        try {
            dataSet = db.showRecent().map { x -> Pair(x.songName,x.artistName) }.toTypedArray()
            Log.w("asdf", "shens code worked WOW!")
        }catch(e:Exception){
            Log.e("asdf", e.toString())
        }
        Log.w("asdf", "connection successful")

        return  dataSet
    }
}