package com.example.playback

import android.provider.BaseColumns

object AcctSchema
{
    class UserEntry : BaseColumns
    {
        companion object
        {
            val TABLE_NAME = "accounts"
            val COLUMN_USER_ID = "user_id"
            val COLUMN_USER_NAME = "user_name"
            val COLUMN_PASSWORD = "password"
        }
    }
}