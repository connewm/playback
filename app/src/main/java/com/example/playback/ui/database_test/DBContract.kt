package com.example.playback.ui.database_test

import android.provider.BaseColumns

object DBContract{
    class DataEntry:BaseColumns{
        companion object {
            val TABLE_NAME = "personalSpotifyDB"
            val COLUMN_RECORD_ID = "recordid"
            val COLUMN_USER_ID = "userid"
            val COLUMN_ARTIST_NAME = "artistname"
            val COLUMN_POPULARITY_SCORE = "popularityscore"
        }
    }
}

