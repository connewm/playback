package com.example.playback

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class SpotifyBroadCastReceiver : BroadcastReceiver() {
    internal object BroadcastTypes {
        const val SPOTIFY_PACKAGE = "com.spotify.music"
        const val PLAYBACK_STATE_CHANGED =  SPOTIFY_PACKAGE + "playbackstatechanged"
        const val QUEUE_CHANGED = SPOTIFY_PACKAGE + "queuechanged"
        const val METADATA_CHANGED = SPOTIFY_PACKAGE + "metadatachanged"
    }

    fun onReceive(context: Context?, intent: Intent) {
        Log.v(TAG, "  onReceive started")

        // This is sent with all broadcasts, regardless of type. The value is taken from
        // System.currentTimeMillis(), which you can compare to in order to determine how
        // old the event is.
        val timeSentInMs: Long = intent.getLongExtra("timeSent", 0L)

        //getting what kind of broad cast has been sent. 3 different types
        val action: String = intent.getAction()

        if (action == BroadcastTypes.METADATA_CHANGED) {
            val trackId: String = intent.getStringExtra("id")
            val artistName: String = intent.getStringExtra("artist")
            val albumName: String = intent.getStringExtra("album")
            val trackName: String = intent.getStringExtra("track")
            val trackLengthInSec: Int = intent.getIntExtra("length", 0)
            Log.v(TAG, "  on Receive track changed to" + artistName)
            // Do something with extracted information...
        } else if (action == BroadcastTypes.PLAYBACK_STATE_CHANGED) {
            val playing: Boolean = intent.getBooleanExtra("playing", false)
            val positionInMs: Int = intent.getIntExtra("playbackPosition", 0)
            // Do something with extracted information
        } else if (action == BroadcastTypes.QUEUE_CHANGED) { // Sent only as a notification, your app may want to respond accordingly.
        }
    }

    companion object {
        private const val TAG = "BroadCastReceiver"
    }
}
