package com.example.playback

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import android.util.Log
import com.example.playback.ui.database_test.DBContract
val TAG = "DBMANAGER"
class DBManager(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME,
        null, DATABASE_VERSION) {

    companion object  {
        private val DATABASE_VERSION = 1
        val DATABASE_NAME = "PersonalSpotifyDB.db"
        private val SQL_CREATE_ENTRIES = ("CREATE TABLE " +
                DBContract.DataEntry.TABLE_NAME + "("
                + DBContract.DataEntry.COLUMN_RECORD_ID + " INTEGER PRIMARY KEY," +
                DBContract.DataEntry.COLUMN_USER_ID + " INTEGER,"
                + DBContract.DataEntry.COLUMN_ARTIST_NAME + " TEXT," + DBContract.DataEntry.COLUMN_POPULARITY_SCORE + " INTEGER," +
                DBContract.DataEntry.COLUMN_SONG_NAME + " TEXT," + DBContract.DataEntry.COLUMN_ALBUM_NAME + " TEXT," + DBContract.DataEntry.COLUMN_SONG_GENRE + " TEXT," +
                DBContract.DataEntry.COLUMN_SONG_LATITUDE + " REAL," + DBContract.DataEntry.COLUMN_SONG_LONGITUDE + " REAL)")
        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.DataEntry.TABLE_NAME
    }

    override fun onCreate(db: SQLiteDatabase) {
        /**SQL statement to create a table**/
        Log.v(TAG, "called onCreate")

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
        values.put(DBContract.DataEntry.COLUMN_SONG_NAME, data.songName)
        values.put(DBContract.DataEntry.COLUMN_ALBUM_NAME, data.albumName)
        values.put(DBContract.DataEntry.COLUMN_SONG_GENRE, data.songGenre)
        values.put(DBContract.DataEntry.COLUMN_SONG_LATITUDE, data.listenLocationLatitude)
        values.put(DBContract.DataEntry.COLUMN_SONG_LONGITUDE, data.listenLocationLongitude)

        try {
            val newRowId = db.insert(DBContract.DataEntry.TABLE_NAME, null, values)
            return true
        } catch(e: Exception) {
            Log.w("asdf", "Add failed in DBManager")
            return false
        }
    }

    // reads all records for a specific user
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
        var songName: String
        var albumName: String
        var songGenre: String
        var songLatitude: Double
        var songLongitude: Double
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                recordid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_RECORD_ID)))
                artistname = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_ARTIST_NAME))
                popularityscore = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_POPULARITY_SCORE)))
                songName = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_SONG_NAME))
                albumName = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_ALBUM_NAME))
                songGenre = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_SONG_GENRE))
                songLatitude = cursor.getDouble(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_SONG_LATITUDE))
                songLongitude = cursor.getDouble(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_SONG_LONGITUDE))

                data.add(SpotifyPersonalData(recordid, userid, artistname, popularityscore, songName, albumName, songGenre, songLatitude, songLongitude))
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

    fun read_specific_record(record_id: Int): SpotifyPersonalData
    {
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.DataEntry.TABLE_NAME + " where " + DBContract.DataEntry.COLUMN_RECORD_ID
                    + "='" + record_id + "'", null)
        } catch(e: SQLiteException)
        {
            db.execSQL(SQL_CREATE_ENTRIES)
            return SpotifyPersonalData(0, 0, "", 0, "", "", "", 0.0, 0.0)
        }
        var user_id: Int = 0
        var artist: String = ""
        var pop: Int = 0
        var songName: String = ""
        var albumName: String = ""
        var songGenre: String = ""
        var songLatitude = 0.0
        var songLongitude = 0.0
        if (cursor!!.moveToFirst())
        {

            user_id = cursor.getInt(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_USER_ID))
            artist = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_ARTIST_NAME))
            pop = cursor.getInt(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_POPULARITY_SCORE))
            songName = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_SONG_NAME))
            albumName = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_ALBUM_NAME))
            songGenre = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_SONG_GENRE))
            songLatitude = cursor.getDouble(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_SONG_LATITUDE))
            songLongitude = cursor.getDouble(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_SONG_LONGITUDE))

        }
        return SpotifyPersonalData(record_id, user_id, artist, pop, songName, albumName, songGenre, songLatitude, songLongitude)
    }

    // record contains the new record to update the old record with
    @Throws(SQLiteConstraintException::class)
    fun update_user_personal(record: SpotifyPersonalData): Boolean
    {
        val db = writableDatabase
        var cursor: Cursor? = null
        var cv: ContentValues = ContentValues()
        cv.put(DBContract.DataEntry.COLUMN_USER_ID, record.userId)
        cv.put(DBContract.DataEntry.COLUMN_ARTIST_NAME, record.artistName)
        cv.put(DBContract.DataEntry.COLUMN_POPULARITY_SCORE, record.popularityScore)
        cv.put(DBContract.DataEntry.COLUMN_SONG_NAME, record.songName)
        cv.put(DBContract.DataEntry.COLUMN_ALBUM_NAME, record.albumName)
        cv.put(DBContract.DataEntry.COLUMN_SONG_GENRE, record.songGenre)
        cv.put(DBContract.DataEntry.COLUMN_SONG_LATITUDE, record.listenLocationLatitude)
        cv.put(DBContract.DataEntry.COLUMN_SONG_LONGITUDE, record.listenLocationLongitude)
        try
        {
            db.update(DBContract.DataEntry.TABLE_NAME, cv, "_id="+record.recordId, null)
        } catch(e: SQLiteException)
        {
            return false
        }
        return true
    }

    // return next record id to be genereated
    @Throws(SQLiteConstraintException::class)
    fun generate_record_id(userid: Int): Int
    {
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.DataEntry.TABLE_NAME + " WHERE " + DBContract.DataEntry.COLUMN_USER_ID + "='" + userid + "' ORDER BY " + DBContract.DataEntry.COLUMN_RECORD_ID,
                null)
        } catch (e: SQLiteException) {
            return -2
        }

        var record_id: Int = -2
        if (cursor!!.moveToLast())
        {
            record_id = cursor.getInt(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_RECORD_ID))
        }

        return record_id + 1
    }
    @Throws(SQLiteConstraintException::class)
    fun showRecent():ArrayList<SpotifyPersonalData>{
        val data = ArrayList<SpotifyPersonalData>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT * FROM" + DBContract.DataEntry.TABLE_NAME + "ORDER BY" + DBContract.DataEntry.COLUMN_RECORD_ID + "DESC LIMIT 10",null)
        }catch (e: SQLiteException) {
            // if table not yet present, create it
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var recordid: Int
        var userid:Int
        var artistname: String
        var popularityscore: Int
        var songName: String
        var albumName: String
        var songGenre: String
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                recordid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_RECORD_ID)))
                userid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_USER_ID)))
                artistname = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_ARTIST_NAME))
                popularityscore = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_POPULARITY_SCORE)))
                songName = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_SONG_NAME))
                albumName = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_ALBUM_NAME))
                songGenre = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_SONG_GENRE))

                data.add(SpotifyPersonalData(recordid, userid,artistname, popularityscore, songName,albumName,songGenre, 0.0 , 0.0))
                cursor.moveToPrevious()
            }
        }
        return data
    }

}