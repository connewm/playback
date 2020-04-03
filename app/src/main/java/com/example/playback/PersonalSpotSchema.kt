package com.example.playback

import android.provider.BaseColumns

object PersonalSpotSchema
{
    class PersonalEntry : BaseColumns
    {
        companion object
        {
            val TABLE_NAME = "personal_spot_data"
            val COL_RECORD_ID = "personal_record_id"
            val COL_USER_ID = "user_id"
            val COL_ARTIST_NAME = "artist_name"
            val COL_POP_SCORE = "personal_popularity_score"
        }
    }
}