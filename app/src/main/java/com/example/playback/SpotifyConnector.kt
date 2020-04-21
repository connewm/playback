package com.example.playback



import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.client.CallResult
import com.spotify.protocol.types.PlayerState

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.ImageUri
import com.spotify.protocol.types.Track;
import java.lang.Exception

class SpotifyConnector {
    //Spotify variables
    private val CLIENT_ID = "f4e7b6f3768a4e3ea9c44e4a5f1d8f9a"
    private val REDIRECT_URI = "com.example.playback://callback"
    var TAG = "SPOTIFY CONNECTOR"

    companion object  {
        private  var mSpotifyAppRemote: SpotifyAppRemote? = null
        private  lateinit var db : DBManager
        private  var albumArt: MutableMap<ImageUri,Bitmap> = mutableMapOf()
    }

    fun connectToSpotify(c: Context){
        //Connect to Spotify
        // Set the connection parameters


            val connectionParams: ConnectionParams = ConnectionParams.Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .showAuthView(true)
                .build()

            SpotifyAppRemote.connect(c, connectionParams,
                object : Connector.ConnectionListener {
                    override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote
                        Log.d(TAG, "Connected! Yay!")
                        // Now you can start interacting with App Remote
                        continuoslyAddSongsToDataBase()
                    }

                    override fun onFailure(throwable: Throwable) {
                        Log.e("MainActivity", throwable.message, throwable)
                        Log.e(TAG, "THIS IS CHEESE!!!")
                        // Something went wrong when attempting to connect! Handle errors here
                    }
                })

    }



    fun disconnectToSpotify(){
        Log.d(TAG, " DISCONNECTED!")
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }



    private fun continuoslyAddSongsToDataBase() {


        // Subscribe to PlayerState
        mSpotifyAppRemote?.playerApi
            ?.subscribeToPlayerState()
            ?.setEventCallback { playerState: PlayerState ->
                val track = playerState.track
                if (track != null) {
                    Log.d(TAG, track.name + " by " + track.artist.name)

                    //TODO get unique id
                    val userId = 0

                    val id = db.generate_record_id(userId) // db.generate_record_id(userId)
                    Log.w("asdf", "$id")

                    val lat: Double = 39.9805
                    val long: Double = -83.0038

                    //get bitmap
                    Log.w("asdf", "IMAGE URI IS " +track.imageUri.toString())
                    //mSpotifyAppRemote!!.imagesApi.getImage(track.imageUri).setResultCallback { b -> albumArt[track.imageUri]  = b }

                    //TODO add data to the database
                    var newData = SpotifyPersonalData(id,userId, track.artist.name.toString(),
                        0,track.name.toString(), track.album.name.toString(),
                        "IDK YET", lat,long, track.imageUri)
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
        mSpotifyAppRemote?.playerApi?.play(s)
    }

    fun getRecentlyPlayed(): List<SpotifyPersonalData>{

        var dataSet: List<SpotifyPersonalData> =  listOf()

        //obtain data from the database
        try {
            dataSet = db.showRecent(20)
            Log.w("asdf", "code worked WOW!")
        }catch(e:Exception){
            Log.e("asdf", e.toString())
        }
        Log.w("asdf", "connection successful")

        return  dataSet
    }

    fun getAlbumArt(uri: ImageUri): Bitmap? {
            var b: Bitmap? = null
            var u = ImageUri("ab67616d0000b273c417aad130701f49d8e629b8")
            Log.d(TAG, "TRYING TO GET ALBUM ART")
            if (albumArt.containsKey(u)){
                b = albumArt!![u]
                Log.d(TAG, "GOT ALBUM ART FROM MAP")
            }
            else if ( isConnected()){

                //get album art
                var u = ImageUri("ab67616d0000b273c417aad130701f49d8e629b8")
                mSpotifyAppRemote!!.imagesApi.getImage(uri).setResultCallback { bit ->
                    albumArt[uri]  = bit
                    b = bit
                }
                Log.d(TAG, "GOT ALBUM ART FROM SPOTIFY")
            }
        return  b
    }


    fun isConnected(): Boolean {
        if( mSpotifyAppRemote != null && mSpotifyAppRemote!!.isConnected)  return true
        return false
    }
}