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
        }
    }
}

