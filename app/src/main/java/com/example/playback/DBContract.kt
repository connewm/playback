package com.example.playback

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
            val COLUMN_SONG_LATITUDE = "song_latitude"
            val COLUMN_SONG_LONGITUDE = "song_longitude"
            val COLUMN_IMAGE_URI = "image_uri"

            // additional table names
            val TABLE_SONG_DATA = "personalSongHistory"
            val TABLE_ARTIST_DATA = "personalArtistHistory"
            val TABLE_ALBUM_DATA = "personalAlbumHistory"


            // additional columns used for the song, artist, and album tables
            val COLUMN_DAILY_LISTEN_START = "daily_listen_start_date"
            val COLUMN_WEEKLY_LISTEN_START = "weekly_listen_start_date"
            val COLUMN_MONTHLY_LISTEN_START = "monthly_listen_start_date"

            val COLUMN_DAILY_LISTENS = "daily_listens"
            val COLUMNN_WEEKLY_LISTENS = "weekly_listens"
            val COLUMN_MONTHLY_LISTENS = "monthly_listens"
            val COLUMN_ALL_TIME_LISTENS = "all_time_listens"
        }
    }
}

