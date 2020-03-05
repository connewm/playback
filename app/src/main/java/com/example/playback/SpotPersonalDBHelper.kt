package com.example.playback

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

import java.util.ArrayList

class SpotPersonalDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
{
    override fun onCreate(db: SQLiteDatabase)
    {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    @Throws(SQLiteConstraintException::class)
    fun insert_personal_record(personal: SpotPersonalData): Boolean
    {
        val db = writableDatabase

        val values = ContentValues()
        values.put(PersonalSpotSchema.PersonalEntry.COL_USER_ID, personal.user_id)
        values.put(PersonalSpotSchema.PersonalEntry.COL_ARTIST_NAME, personal.artist_name)
        values.put(PersonalSpotSchema.PersonalEntry.COL_POP_SCORE, personal.popularity_score)

        val new_row_id = db.insert(PersonalSpotSchema.PersonalEntry.TABLE_NAME, null, values)
        return true

    }

    @Throws(SQLiteConstraintException::class)
    fun delete_personal_record(user_id: Int): Boolean
    {
        val db = writableDatabase

        val selection = PersonalSpotSchema.PersonalEntry.COL_USER_ID + " LIKE ?"
        val selection_args = arrayOf(user_id.toString())

        db.delete(PersonalSpotSchema.PersonalEntry.TABLE_NAME, selection, selection_args)
        return true
    }

    // if the record id for a personal record is available, this will read only one record
    // (record_id is the primary key)
    fun read_specific_record(record_id: Int): SpotPersonalData?
    {
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + PersonalSpotSchema.PersonalEntry.TABLE_NAME + " where " + PersonalSpotSchema.PersonalEntry.COL_RECORD_ID
            + "='" + record_id + "'", null)
        } catch(e: SQLiteException)
        {
            db.execSQL(SQL_CREATE_ENTRIES)
            return null
        }
        var user_id: Int = 0
        var artist: String = ""
        var pop: Int = 0
        if (cursor!!.moveToFirst())
        {

                user_id = cursor.getInt(cursor.getColumnIndex(PersonalSpotSchema.PersonalEntry.COL_USER_ID))
                artist = cursor.getString(cursor.getColumnIndex(PersonalSpotSchema.PersonalEntry.COL_ARTIST_NAME))
                pop = cursor.getInt(cursor.getColumnIndex(PersonalSpotSchema.PersonalEntry.COL_POP_SCORE))

        }
        return SpotPersonalData(record_id, user_id, artist, pop)
    }

    // read all records for that specific user (will likely be multiple)
    fun read_user_personal_data(user_id: Int): ArrayList<SpotPersonalData>
    {
        val personal_records = ArrayList<SpotPersonalData>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + PersonalSpotSchema.PersonalEntry.TABLE_NAME + " where " + PersonalSpotSchema.PersonalEntry.COL_USER_ID + "='" + user_id
            +"'", null)
        } catch(e: SQLiteException)
        {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var artist: String
        var pop: Int
        var record_id: Int
        if (cursor!!.moveToFirst())
        {
            while(!cursor.isAfterLast)
            {
                record_id = cursor.getInt(cursor.getColumnIndex(PersonalSpotSchema.PersonalEntry.COL_RECORD_ID))
                artist = cursor.getString(cursor.getColumnIndex(PersonalSpotSchema.PersonalEntry.COL_ARTIST_NAME))
                pop = cursor.getInt(cursor.getColumnIndex(PersonalSpotSchema.PersonalEntry.COL_POP_SCORE))
                personal_records.add(SpotPersonalData(record_id, user_id, artist, pop))
                cursor.moveToNext()
            }
        }
        return personal_records
    }

    // record contains the new record to update the old record with
    @Throws(SQLiteConstraintException::class)
    fun update_user_personal(record: SpotPersonalData): Boolean
    {
        val db = writableDatabase
        var cursor: Cursor? = null
        var cv: ContentValues = ContentValues()
        cv.put(PersonalSpotSchema.PersonalEntry.COL_USER_ID, record.user_id)
        cv.put(PersonalSpotSchema.PersonalEntry.COL_ARTIST_NAME, record.artist_name)
        cv.put(PersonalSpotSchema.PersonalEntry.COL_POP_SCORE, record.popularity_score)
        try
        {
            db.update(PersonalSpotSchema.PersonalEntry.TABLE_NAME, cv, "_id="+record.record_id, null)
        } catch(e: SQLiteException)
        {
            return false
        }
        return true
    }

    companion object
    {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "PersonalRecords.db"

        private val SQL_CREATE_ENTRIES = "create table if not exists" + PersonalSpotSchema.PersonalEntry.TABLE_NAME +
                " ("+ PersonalSpotSchema.PersonalEntry.COL_RECORD_ID + " integer primary key," + PersonalSpotSchema.PersonalEntry.COL_USER_ID + " integer," + PersonalSpotSchema.PersonalEntry.COL_ARTIST_NAME +
                " text," + PersonalSpotSchema.PersonalEntry.COL_POP_SCORE + " text)"

        private val SQL_DELETE_ENTRIES = "drop table if exists " + PersonalSpotSchema.PersonalEntry.TABLE_NAME
    }
}