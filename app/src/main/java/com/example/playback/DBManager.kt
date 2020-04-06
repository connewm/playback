package com.example.playback

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import com.example.playback.ui.database_test.DBContract

class DBManager(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME,
        null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        val DATABASE_NAME = "PersonalSpotifyDB.db"
        const val tablePersonalData = "PersonalData"
        private val SQL_CREATE_ENTRIES = ("CREATE TABLE " +
                tablePersonalData + "("
                + DBContract.DataEntry.COLUMN_RECORD_ID + " INTEGER PRIMARY KEY," +
                DBContract.DataEntry.COLUMN_USER_ID + " INTEGER PRIMARY KEY,"
                + DBContract.DataEntry.COLUMN_ARTIST_NAME + " TEXT," + DBContract.DataEntry.COLUMN_POPULARITY_SCORE + " INTEGER" + ")")
        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.DataEntry.TABLE_NAME
    }

    override fun onCreate(db: SQLiteDatabase) {
        /**SQL statement to create a table**/
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int,
                           newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    @Throws(SQLiteConstraintException::class)
    fun addData(data : SpotifyPersonalData):Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(DBContract.DataEntry.COLUMN_RECORD_ID, data.recordId)
        values.put(DBContract.DataEntry.COLUMN_USER_ID, data.userId)
        values.put(DBContract.DataEntry.COLUMN_ARTIST_NAME, data.artistName)
        values.put(DBContract.DataEntry.COLUMN_POPULARITY_SCORE, data.popularityScore)

        val newRowId = db.insert(DBContract.DataEntry.TABLE_NAME, null, values)
        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun findData(userid: Int): ArrayList<SpotifyPersonalData> {
        val data = ArrayList<SpotifyPersonalData>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.DataEntry.TABLE_NAME + " WHERE " + DBContract.DataEntry.COLUMN_USER_ID + "='" + userid + "'", null)
        } catch (e: SQLiteException) {
            // if table not yet present, create it
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var recordid: Int
        var artistname: String
        var popularityscore: Int
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                recordid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_RECORD_ID)))
                artistname = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_ARTIST_NAME))
                popularityscore = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_POPULARITY_SCORE)))

                data.add(SpotifyPersonalData(recordid, userid, artistname, popularityscore))
                cursor.moveToNext()
            }
        }
        return data
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteData(userid: Int): Boolean {

        // Gets the data repository in write mode
        val db = writableDatabase
        // Define 'where' part of query.
        val selection = DBContract.DataEntry.COLUMN_USER_ID + " LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(userid.toString())
        // Issue SQL statement.
        db.delete(DBContract.DataEntry.TABLE_NAME, selection, selectionArgs)

        return true
    }

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
    fun read_user_personal_data(user_id: Int): java.util.ArrayList<SpotPersonalData>
    {
        val personal_records = java.util.ArrayList<SpotPersonalData>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + PersonalSpotSchema.PersonalEntry.TABLE_NAME + " where " + PersonalSpotSchema.PersonalEntry.COL_USER_ID + "='" + user_id
                    +"'", null)
        } catch(e: SQLiteException)
        {
            db.execSQL(SQL_CREATE_ENTRIES)
            return java.util.ArrayList()
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
}