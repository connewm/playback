package com.example.playback.ui.database_test

import android.provider.BaseColumns

object DBContract{
    class DataEntry:BaseColumns{
        companion object {
            val TABLE_NAME = "personalSpotifyDB"
            val COLUMN_RECORD_ID = "record_id"
            val COLUMN_USER_ID = "user_id"
            val COLUMN_ARTIST_NAME = "artist_name"
            val COLUMN_POPULARITY_SCORE = "popularity_score"
            val COLUMN_SONG_NAME = "song_name"
            val COLUMN_ALBUM_NAME = "album_name"
            val COLUMN_SONG_GENRE = "song_genre"
<<<<<<< HEAD
=======
            val COLUMN_SONG_LATITUDE = "song_latitude"
            val COLUMN_SONG_LONGITUDE = "song_longitude"
>>>>>>> 51d5a2b16e8c9d55e490dd94ae86d348a0758f3c
        }
    }
}

