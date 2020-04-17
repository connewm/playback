package com.example.playback

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

val TAG = "DBMANAGER"

class DBManager(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME,
        null, DATABASE_VERSION) {
    var db = this.writableDatabase

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

        private val CREATE_SONG_ENTRIES = ("create table " + DBContract.DataEntry.TABLE_SONG_DATA + "(" +
                DBContract.DataEntry.COLUMN_SONG_NAME + " text primary key, " + DBContract.DataEntry.COLUMN_ARTIST_NAME + " text, "+ DBContract.DataEntry.COLUMN_DAILY_LISTEN_START +
                " text, " + DBContract.DataEntry.COLUMN_WEEKLY_LISTEN_START + " text, " + DBContract.DataEntry.COLUMN_MONTHLY_LISTEN_START + " text, " +
                DBContract.DataEntry.COLUMN_DAILY_LISTENS + " integer, " + DBContract.DataEntry.COLUMNN_WEEKLY_LISTENS + " integer, " + DBContract.DataEntry.COLUMN_MONTHLY_LISTENS + " integer, "
                + DBContract.DataEntry.COLUMN_ALL_TIME_LISTENS + " integer)")

        private val CREATE_ARTIST_ENTRIES = ("create table " + DBContract.DataEntry.TABLE_ARTIST_DATA + "(" +
                DBContract.DataEntry.COLUMN_ARTIST_NAME + " text primary key, " + DBContract.DataEntry.COLUMN_DAILY_LISTEN_START +
                " text, " + DBContract.DataEntry.COLUMN_WEEKLY_LISTEN_START + " text, " + DBContract.DataEntry.COLUMN_MONTHLY_LISTEN_START + " text, " +
                DBContract.DataEntry.COLUMN_DAILY_LISTENS + " integer, " + DBContract.DataEntry.COLUMNN_WEEKLY_LISTENS + " integer, " + DBContract.DataEntry.COLUMN_MONTHLY_LISTENS + " integer, "
                + DBContract.DataEntry.COLUMN_ALL_TIME_LISTENS + " integer)")

        private val CREATE_ALBUM_ENTRIES = ("create table " + DBContract.DataEntry.TABLE_ALBUM_DATA + "(" +
                DBContract.DataEntry.COLUMN_ALBUM_NAME + " text primary key, " + DBContract.DataEntry.COLUMN_ARTIST_NAME + " text, " + DBContract.DataEntry.COLUMN_DAILY_LISTEN_START +
                " text, " + DBContract.DataEntry.COLUMN_WEEKLY_LISTEN_START + " text, " + DBContract.DataEntry.COLUMN_MONTHLY_LISTEN_START + " text, " +
                DBContract.DataEntry.COLUMN_DAILY_LISTENS + " integer, " + DBContract.DataEntry.COLUMNN_WEEKLY_LISTENS + " integer, " + DBContract.DataEntry.COLUMN_MONTHLY_LISTENS + " integer, "
                + DBContract.DataEntry.COLUMN_ALL_TIME_LISTENS + " integer)")
        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.DataEntry.TABLE_NAME
    }


    override fun onCreate(db: SQLiteDatabase) {
        /**SQL statement to create a table**/
        Log.v(TAG, "called onCreate")

        try {
            db.execSQL(SQL_CREATE_ENTRIES)
            db.execSQL(CREATE_SONG_ENTRIES)
            db.execSQL(CREATE_ARTIST_ENTRIES)
            db.execSQL(CREATE_ALBUM_ENTRIES)
            Log.w("database creation", "creation of main and add. tables good")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.w("asdf", "creation not working for one of the tables")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int,
                           newVersion: Int) {
        try {
            db.execSQL(SQL_DELETE_ENTRIES)
            Log.w("database creation", "creation of main and add. tables good")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    // only inserting unique songs
    @Throws(SQLiteConstraintException::class)
    fun addData(data : SpotifyPersonalData):Boolean {

        // first make sure the song is not in the db already
        val db = this.writableDatabase
        var cursor: Cursor? = null
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = sdf.format(Date())
        // add listen record in the various sub tables for today's data
        var query = ""
        try {
            cursor = db.rawQuery("select * from " + DBContract.DataEntry.TABLE_SONG_DATA + " where " + DBContract.DataEntry.COLUMN_SONG_NAME + " = " + "\"" + data.songName + "\"", null)
        } catch(e: SQLiteException) {
            e.printStackTrace()
            Log.w("asdf", "song select from after main failed when adding to subtable")
            cursor!!.close()
            return false
        }
        Log.w("asdf", "song ind. cursor count is ${cursor.count}")
        if (cursor.count == 0)
        {
            addSongListen(data.songName, data.artistName, currentDate)
            Log.w("asdf", "adding ${data.songName}")
            Log.w("asdf", "Cursor count is ${cursor.count}")
            Log.w("asdf", "possible problem with $query")
        } else {
            updateSongListen(data.songName, currentDate)
            Log.w("asdf", "In song listen block")
        }


        // must check if artist already in db
        try {
            cursor = db.rawQuery("select * from " + DBContract.DataEntry.TABLE_ARTIST_DATA + " where " + DBContract.DataEntry.COLUMN_ARTIST_NAME + " = " + "\""+data.artistName+"\"", null)
        } catch(e: SQLiteException) {
            e.printStackTrace()
            Log.w("asdf", "artist select from main failed when adding to subtable")
            cursor.close()
            return false
        }
        if (cursor.count <= 0) {
            // not only has song not been added but neither has artist
            addArtistListen(data.artistName, currentDate)
        } else {
            updateArtistListen(data.artistName, currentDate)
        }

        // check if album in db already
        try {
            cursor = db.rawQuery("select * from " + DBContract.DataEntry.TABLE_ALBUM_DATA + " where " + DBContract.DataEntry.COLUMN_ALBUM_NAME + " = " + "\""+data.albumName+"\"", null)
        } catch(e: SQLiteException) {
            e.printStackTrace()
            Log.w("asdf", "artist select from main failed when adding to subtable")
            cursor.close()
            return false
        }

        if (cursor.count <= 0) {
            addAlbumListen(data.albumName, data.artistName, currentDate)
        } else {
            updateAlbumListen(data.albumName, currentDate)
        }


        try {
            cursor = db.rawQuery("select * from " + DBContract.DataEntry.TABLE_NAME + " where " + DBContract.DataEntry.COLUMN_SONG_NAME + " = " + "\"" + data.songName + "\"", null)
        } catch(e: SQLiteException) {
            e.printStackTrace()
            Log.w("asdf", "select song from main failed before adding song to subtable")
            cursor!!.close()
            return false
        }

        Log.w("asdf", "In add")
        Log.w("adsf", "Cursor count at outer block is ${cursor.count}")
        // there is no record of that song yet
        if (cursor.count == 0) {
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
            Log.w("Adding new song to db", "Adding")

            try {
                val newRowId = db.insert(DBContract.DataEntry.TABLE_NAME, null, values)
            } catch (e: Exception) {
                Log.w("asdf", "Add failed in DBManager")
                cursor.close()
                return false
            }
        }
        cursor.close()
        return true

    }



    // record already exists, update the listen numbers conditionally
    @Throws(SQLiteException::class)
    fun updateSongListen(songName: String, dateUpdating: String): Boolean {
        // first select values from db so we can see if the listens values need to be cleared
        // or just updated
        val db = writableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery("select * from " + DBContract.DataEntry.TABLE_SONG_DATA + " WHERE " + DBContract.DataEntry.COLUMN_SONG_NAME + " = " + "\""+songName+"\"", null)
        } catch (e: SQLiteException) {
            // if table not yet present, create it
            e.printStackTrace()
            cursor!!.close()
            return false
        }

        var prevDateDaily: String = ""
        var prevDateWeekly: String = ""
        var prevDateMontly: String = ""

        var prevDailyListens: Int = 0
        var prevMontlyListens: Int = 0
        var prevWeeklyListens: Int = 0
        var prevAllTimeListens: Int = 0

        if (cursor!!.moveToFirst())
        {
            Log.w("adsf", "returned a result from main table add")
            prevDateDaily = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_DAILY_LISTEN_START))
            prevDateWeekly = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_WEEKLY_LISTEN_START))
            prevDateMontly = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_MONTHLY_LISTEN_START))

            prevDailyListens = cursor.getInt(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_DAILY_LISTENS))
            prevWeeklyListens = cursor.getInt(cursor.getColumnIndex(DBContract.DataEntry.COLUMNN_WEEKLY_LISTENS))
            prevMontlyListens = cursor.getInt(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_MONTHLY_LISTENS))
            prevAllTimeListens = cursor.getInt(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_ALL_TIME_LISTENS))
        }

        // see if one day has passed from db daily date for song: clear if so, update if not

        val currentDateDay: Int = Integer.parseInt(dateUpdating.substring(0,2))
        val currentDateMonth: Int = Integer.parseInt(dateUpdating.substring(3, 5))

        // "new" date values may remain the same depending on the below if statements
        // I initialize all of them even though they may remain unchanged so I can always
        // do only ONE update operation instead of three separate ones
        var newDailyStart: String = ""
        var newWeeklyStart: String = ""
        var newMontlyStart: String = ""

        // do daily

        if ((currentDateDay != Integer.parseInt(prevDateDaily.substring(0,2))) || (currentDateMonth != Integer.parseInt(prevDateDaily.substring(3,5))))
        {
            newDailyStart = dateUpdating
            prevDailyListens = 1
            Log.w("asdf", "Not updating listens for $songName")
        } else {
            prevDailyListens += 1
            newDailyStart = prevDateDaily
            Log.w("asdf", "updated song $songName listens")
        }

        // do weekly
        if (((currentDateDay - Integer.parseInt(prevDateWeekly.substring(0,2))) > 7) || (currentDateMonth != Integer.parseInt(prevDateWeekly.substring(3,5)))) {
            newWeeklyStart = dateUpdating
            prevWeeklyListens = 1
        } else {
            newWeeklyStart = prevDateWeekly
            prevWeeklyListens += 1
        }

        // do Monthly
        if (currentDateMonth != Integer.parseInt(prevDateMontly.substring(3,5))) {
            newMontlyStart = dateUpdating
            prevMontlyListens = 1
        } else {
            newMontlyStart = prevDateMontly
            prevMontlyListens += 1
        }

        Log.w("asdf", "made it to end of if/else")
        // update all time no matter what
        prevAllTimeListens += 1

        // perform db udpate
        var values = ContentValues()
        values.put(DBContract.DataEntry.COLUMN_DAILY_LISTEN_START, newDailyStart)
        values.put(DBContract.DataEntry.COLUMN_DAILY_LISTENS, prevDailyListens)
        values.put(DBContract.DataEntry.COLUMN_WEEKLY_LISTEN_START, newWeeklyStart)
        values.put(DBContract.DataEntry.COLUMNN_WEEKLY_LISTENS, prevWeeklyListens)
        values.put(DBContract.DataEntry.COLUMN_MONTHLY_LISTEN_START, newMontlyStart)
        values.put(DBContract.DataEntry.COLUMN_MONTHLY_LISTENS, prevMontlyListens)

        try {
            db.update(DBContract.DataEntry.TABLE_SONG_DATA, values,"${DBContract.DataEntry.COLUMN_SONG_NAME}=\"$songName\"", null)
            Log.w("asdf", "update performed successfully to song")
        } catch(e: SQLiteException)
        {
            e.printStackTrace()
            Log.w("asdf", "failed updating song subtable")
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }

    // update for artists
    @Throws(SQLiteException::class)
    fun updateArtistListen(artist: String, dateUpdating:String) : Boolean {
        // first select values from db so we can see if the listens values need to be cleared
        // or just updated
        val db = writableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery("select * from " + DBContract.DataEntry.TABLE_ARTIST_DATA + " WHERE " + DBContract.DataEntry.COLUMN_ARTIST_NAME + " = " + "\""+artist+"\"", null)
        } catch (e: SQLiteException) {
            // if table not yet present, create it
            e.printStackTrace()
            cursor!!.close()
            return false
        }

        var prevDateDaily: String = ""
        var prevDateWeekly: String = ""
        var prevDateMontly: String = ""

        var prevDailyListens: Int = 0
        var prevMontlyListens: Int = 0
        var prevWeeklyListens: Int = 0
        var prevAllTimeListens: Int = 0

        if (cursor!!.moveToFirst())
        {
            prevDateDaily = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_DAILY_LISTEN_START))
            prevDateWeekly = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_WEEKLY_LISTEN_START))
            prevDateMontly = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_MONTHLY_LISTEN_START))

            prevDailyListens = cursor.getInt(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_DAILY_LISTENS))
            prevWeeklyListens = cursor.getInt(cursor.getColumnIndex(DBContract.DataEntry.COLUMNN_WEEKLY_LISTENS))
            prevMontlyListens = cursor.getInt(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_MONTHLY_LISTENS))
            prevAllTimeListens = cursor.getInt(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_ALL_TIME_LISTENS))
        }

        // see if one day has passed from db daily date for song: clear if so, update if not
        val currentDateDay: Int = Integer.parseInt(dateUpdating.substring(0,2))
        val currentDateMonth: Int = Integer.parseInt(dateUpdating.substring(3, 5))

        // "new" date values may remain the same depending on the below if statements
        // I initialize all of them even though they may remain unchanged so I can always
        // do only ONE update operation instead of three separate ones
        var newDailyStart: String = ""
        var newWeeklyStart: String = ""
        var newMontlyStart: String = ""

        // do daily
        if ((currentDateDay != Integer.parseInt(prevDateDaily.substring(0,2))) || (currentDateMonth != Integer.parseInt(prevDateDaily.substring(3,5))))
        {
            newDailyStart = dateUpdating
            prevDailyListens = 1
        } else {
            prevDailyListens += 1
            newDailyStart = prevDateDaily
        }

        // do weekly
        if (((currentDateDay - Integer.parseInt(prevDateWeekly.substring(0,2))) > 7) || (currentDateMonth != Integer.parseInt(prevDateWeekly.substring(3,5)))) {
            newWeeklyStart = dateUpdating
            prevWeeklyListens = 1
        } else {
            prevWeeklyListens += 1
            newWeeklyStart = prevDateWeekly
        }

        // do Monthly
        if (currentDateMonth != Integer.parseInt(prevDateMontly.substring(3,5))) {
            newMontlyStart = dateUpdating
            prevMontlyListens = 1
        } else {
            prevMontlyListens += 1
            newMontlyStart = prevDateMontly
        }

        // update all time no matter what
        prevAllTimeListens += 1

        // perform db udpate
        var values = ContentValues()
        values.put(DBContract.DataEntry.COLUMN_DAILY_LISTEN_START, newDailyStart)
        values.put(DBContract.DataEntry.COLUMN_DAILY_LISTENS, prevDailyListens)
        values.put(DBContract.DataEntry.COLUMN_WEEKLY_LISTEN_START, newWeeklyStart)
        values.put(DBContract.DataEntry.COLUMNN_WEEKLY_LISTENS, prevWeeklyListens)
        values.put(DBContract.DataEntry.COLUMN_MONTHLY_LISTEN_START, newMontlyStart)
        values.put(DBContract.DataEntry.COLUMN_MONTHLY_LISTENS, prevMontlyListens)

        try {
            db.update(DBContract.DataEntry.TABLE_ARTIST_DATA, values,"${DBContract.DataEntry.COLUMN_ARTIST_NAME}=\"$artist\"", null)
        } catch(e: SQLiteException)
        {
            e.printStackTrace()
            Log.w("asdf", "update failed when in artist subtable")
            cursor!!.close()
            return false
        }
        cursor.close()
        return true
    }

    // update listens for album
    @Throws(SQLiteException::class)
    fun updateAlbumListen(albumName: String, dateUpdating: String) : Boolean {
        // first select values from db so we can see if the listens values need to be cleared
        // or just updated
        val db = writableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery("select * from " + DBContract.DataEntry.TABLE_ALBUM_DATA + " WHERE " + DBContract.DataEntry.COLUMN_ALBUM_NAME + " = " + "\""+albumName+"\"", null)
        } catch (e: SQLiteException) {
            // if table not yet present, create it
            e.printStackTrace()
            cursor!!.close()
            return false
        }

        var prevDateDaily: String = ""
        var prevDateWeekly: String = ""
        var prevDateMontly: String = ""

        var prevDailyListens: Int = 0
        var prevMontlyListens: Int = 0
        var prevWeeklyListens: Int = 0
        var prevAllTimeListens: Int = 0

        if (cursor!!.moveToFirst())
        {
            prevDateDaily = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_DAILY_LISTEN_START))
            prevDateWeekly = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_WEEKLY_LISTEN_START))
            prevDateMontly = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_MONTHLY_LISTEN_START))

            prevDailyListens = cursor.getInt(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_DAILY_LISTENS))
            prevWeeklyListens = cursor.getInt(cursor.getColumnIndex(DBContract.DataEntry.COLUMNN_WEEKLY_LISTENS))
            prevMontlyListens = cursor.getInt(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_MONTHLY_LISTENS))
            prevAllTimeListens = cursor.getInt(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_ALL_TIME_LISTENS))
        }

        // see if one day has passed from db daily date for song: clear if so, update if not
        val currentDateDay: Int = Integer.parseInt(dateUpdating.substring(0,2))
        val currentDateMonth: Int = Integer.parseInt(dateUpdating.substring(3, 5))

        // "new" date values may remain the same depending on the below if statements
        // I initialize all of them even though they may remain unchanged so I can always
        // do only ONE update operation instead of three separate ones
        var newDailyStart: String = ""
        var newWeeklyStart: String = ""
        var newMontlyStart: String = ""

        // do daily
        if ((currentDateDay != Integer.parseInt(prevDateDaily.substring(0,2))) || (currentDateMonth != Integer.parseInt(prevDateDaily.substring(3,5))))
        {
            newDailyStart = dateUpdating
            prevDailyListens = 1
        } else {
            prevDailyListens += 1
            newDailyStart = prevDateDaily
        }

        // do weekly
        if (((currentDateDay - Integer.parseInt(prevDateWeekly.substring(0,2))) > 7) || (currentDateMonth != Integer.parseInt(prevDateWeekly.substring(3,5)))) {
            newWeeklyStart = dateUpdating
            prevWeeklyListens = 1
        } else {
            prevWeeklyListens += 1
            newWeeklyStart = prevDateWeekly
        }

        // do Monthly
        if (currentDateMonth != Integer.parseInt(prevDateMontly.substring(3,5))) {
            newMontlyStart = dateUpdating
            prevMontlyListens = 1
        } else {
            prevMontlyListens += 1
            newMontlyStart = prevDateMontly
        }

        // update all time no matter what
        prevAllTimeListens += 1

        // perform db udpate
        var values = ContentValues()
        values.put(DBContract.DataEntry.COLUMN_DAILY_LISTEN_START, newDailyStart)
        values.put(DBContract.DataEntry.COLUMN_DAILY_LISTENS, prevDailyListens)
        values.put(DBContract.DataEntry.COLUMN_WEEKLY_LISTEN_START, newWeeklyStart)
        values.put(DBContract.DataEntry.COLUMNN_WEEKLY_LISTENS, prevWeeklyListens)
        values.put(DBContract.DataEntry.COLUMN_MONTHLY_LISTEN_START, newMontlyStart)
        values.put(DBContract.DataEntry.COLUMN_MONTHLY_LISTENS, prevMontlyListens)

        try {
            db.update(DBContract.DataEntry.TABLE_ALBUM_DATA, values,"${DBContract.DataEntry.COLUMN_ALBUM_NAME}=\"$albumName\"", null)
        } catch(e: SQLiteException)
        {
            e.printStackTrace()
            Log.w("asdf", "album update failed when in album subtable")
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }

    // dateAdding lets the db know what the date is so it can create a new record with that date
    @Throws(SQLiteException::class)
    fun addSongListen(songName: String, artistName: String, dateAdding: String): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put(DBContract.DataEntry.COLUMN_SONG_NAME, "$songName")
        values.put(DBContract.DataEntry.COLUMN_ARTIST_NAME, "$artistName")
        values.put(DBContract.DataEntry.COLUMN_DAILY_LISTEN_START, dateAdding)
        values.put(DBContract.DataEntry.COLUMN_WEEKLY_LISTEN_START, dateAdding)
        values.put(DBContract.DataEntry.COLUMN_MONTHLY_LISTEN_START, dateAdding)
        values.put(DBContract.DataEntry.COLUMN_DAILY_LISTENS, 1)
        values.put(DBContract.DataEntry.COLUMNN_WEEKLY_LISTENS, 1)
        values.put(DBContract.DataEntry.COLUMN_MONTHLY_LISTENS, 1)
        values.put(DBContract.DataEntry.COLUMN_ALL_TIME_LISTENS, 1)

        try {
            val newRowId = db.insert(DBContract.DataEntry.TABLE_SONG_DATA, null, values)
            return true
        } catch(e: Exception) {
            Log.w("song addition", "Adding song failed")
            return false
        }

    }

    // dateAdding lets the db know what the date is so it can create a new record with that date
    @Throws(SQLiteException::class)
    fun addArtistListen(artistName: String, dateAdding: String): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put(DBContract.DataEntry.COLUMN_ARTIST_NAME, artistName)
        values.put(DBContract.DataEntry.COLUMN_DAILY_LISTEN_START, dateAdding)
        values.put(DBContract.DataEntry.COLUMN_WEEKLY_LISTEN_START, dateAdding)
        values.put(DBContract.DataEntry.COLUMN_MONTHLY_LISTEN_START, dateAdding)
        values.put(DBContract.DataEntry.COLUMN_DAILY_LISTENS, 1)
        values.put(DBContract.DataEntry.COLUMNN_WEEKLY_LISTENS, 1)
        values.put(DBContract.DataEntry.COLUMN_MONTHLY_LISTENS, 1)
        values.put(DBContract.DataEntry.COLUMN_ALL_TIME_LISTENS, 1)

        try {
            val newRowId = db.insert(DBContract.DataEntry.TABLE_ARTIST_DATA, null, values)
            return true
        } catch(e: Exception) {
            Log.w("artist addition", "Adding artist failed")
            return false
        }

    }

    // dateAdding lets the db know what the date is so it can create a new record with that date
    @Throws(SQLiteException::class)
    fun addAlbumListen(albumName: String, artistName: String, dateAdding: String): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put(DBContract.DataEntry.COLUMN_ALBUM_NAME, albumName)
        values.put(DBContract.DataEntry.COLUMN_ARTIST_NAME, artistName)
        values.put(DBContract.DataEntry.COLUMN_DAILY_LISTEN_START, dateAdding)
        values.put(DBContract.DataEntry.COLUMN_WEEKLY_LISTEN_START, dateAdding)
        values.put(DBContract.DataEntry.COLUMN_MONTHLY_LISTEN_START, dateAdding)
        values.put(DBContract.DataEntry.COLUMN_DAILY_LISTENS, 1)
        values.put(DBContract.DataEntry.COLUMNN_WEEKLY_LISTENS, 1)
        values.put(DBContract.DataEntry.COLUMN_MONTHLY_LISTENS, 1)
        values.put(DBContract.DataEntry.COLUMN_ALL_TIME_LISTENS, 1)

        try {
            val newRowId = db.insert(DBContract.DataEntry.TABLE_ALBUM_DATA, null, values)
            return true
        } catch(e: Exception) {
            Log.w("album addition", "Adding album failed")
            return false
        }

    }


    // may still be useful for recycler view; not sure but beware it will only return unique song listens
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
            cursor!!.close()
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
                recordid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(
                    DBContract.DataEntry.COLUMN_RECORD_ID)))
                artistname = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_ARTIST_NAME))
                popularityscore = Integer.parseInt(cursor.getString(cursor.getColumnIndex(
                    DBContract.DataEntry.COLUMN_POPULARITY_SCORE)))
                songName = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_SONG_NAME))
                albumName = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_ALBUM_NAME))
                songGenre = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_SONG_GENRE))
                songLatitude = cursor.getDouble(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_SONG_LATITUDE))
                songLongitude = cursor.getDouble(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_SONG_LONGITUDE))

                data.add(SpotifyPersonalData(recordid, userid, artistname, popularityscore, songName, albumName, songGenre, songLatitude, songLongitude))
                cursor.moveToNext()
            }
        }
        cursor.close()
        return data
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteData(userid: Int): Boolean {

        // Gets the data repository in write mode
        val db = writableDatabase
        try {
            // Define 'where' part of query.
            db.rawQuery("drop table if exists " + DBContract.DataEntry.TABLE_NAME, null)
            db.rawQuery("drop table if exists " + DBContract.DataEntry.TABLE_SONG_DATA, null)
            db.rawQuery("drop table if exists " + DBContract.DataEntry.TABLE_ARTIST_DATA, null)
            db.rawQuery("drop table if exists " + DBContract.DataEntry.TABLE_ALBUM_DATA, null)
            Log.w("asdf", "delete worked")
        } catch(e:SQLiteException) {
            Log.w("delete was unsuccessful", "delete not working")
        }

        return true
    }

    /**
     * Methods for pulling data from the individual tables. I will use the HistoryRecord object to create these responses
     */

    @Throws(SQLiteException::class)
    fun getSongs(timeframe: Int): ArrayList<HistoryRecord> {
        var data = ArrayList<HistoryRecord>()
        val db = writableDatabase
        var cursor: Cursor? = null

        if (timeframe == HistoryRecord.TIME_DAILY) {
            try {
                cursor = db.rawQuery("select " + DBContract.DataEntry.COLUMN_SONG_NAME + ", " + DBContract.DataEntry.COLUMN_ARTIST_NAME
                        + ", "+ DBContract.DataEntry.COLUMN_DAILY_LISTENS + " from " + DBContract.DataEntry.TABLE_SONG_DATA + " order by " + DBContract.DataEntry.COLUMN_DAILY_LISTENS + " desc"
                , null)
            } catch(e: SQLiteException) {
                e.printStackTrace()
                Log.w("asdf", "Error retrieving daily song listening")
            }
        } else if (timeframe == HistoryRecord.TIME_WEEKLY) {
            try {
                cursor = db.rawQuery("select " + DBContract.DataEntry.COLUMN_SONG_NAME + ", " + DBContract.DataEntry.COLUMN_ARTIST_NAME
                        + ", "+ DBContract.DataEntry.COLUMNN_WEEKLY_LISTENS + " from " + DBContract.DataEntry.TABLE_SONG_DATA + " order by " + DBContract.DataEntry.COLUMNN_WEEKLY_LISTENS + " desc"
                    , null)
            } catch(e: SQLiteException) {
                e.printStackTrace()
                Log.w("asdf", "Error retrieving weekly song listening")
            }
        } else if (timeframe == HistoryRecord.TIME_MONTHLY) {
            try {
                cursor = db.rawQuery("select " + DBContract.DataEntry.COLUMN_SONG_NAME + ", " + DBContract.DataEntry.COLUMN_ARTIST_NAME
                        + ", "+ DBContract.DataEntry.COLUMN_MONTHLY_LISTENS + " from " + DBContract.DataEntry.TABLE_SONG_DATA + " order by " + DBContract.DataEntry.COLUMN_MONTHLY_LISTENS + " desc"
                    , null)
            } catch(e: SQLiteException) {
                e.printStackTrace()
                Log.w("asdf", "Error retrieving monthly song listening")
            }
        } else if (timeframe == HistoryRecord.TIME_ALL) {
            try {
                cursor = db.rawQuery("select " + DBContract.DataEntry.COLUMN_SONG_NAME + ", " + DBContract.DataEntry.COLUMN_ARTIST_NAME
                        + ", "+ DBContract.DataEntry.COLUMN_ALL_TIME_LISTENS + " from " + DBContract.DataEntry.TABLE_SONG_DATA + " order by " + DBContract.DataEntry.COLUMN_ALL_TIME_LISTENS + " desc"
                    , null)
            } catch(e: SQLiteException) {
                e.printStackTrace()
                Log.w("asdf", "Error retrieving all time song listening")
            }
        } else {
            Log.w("asdf", "Error: Invalid code passed")
            cursor!!.close()
            return ArrayList<HistoryRecord>()
        }

        Log.w("asdf", "cursor count when actually pulling from db ${cursor!!.count}")

        if (cursor!!.moveToLast()) {
            while (!cursor.isBeforeFirst) {
                var song = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_SONG_NAME))
                var artist = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_ARTIST_NAME))
                var index: String
                when(timeframe) {
                    HistoryRecord.TIME_DAILY -> index = DBContract.DataEntry.COLUMN_DAILY_LISTENS
                    HistoryRecord.TIME_WEEKLY -> index = DBContract.DataEntry.COLUMNN_WEEKLY_LISTENS
                    HistoryRecord.TIME_MONTHLY -> index = DBContract.DataEntry.COLUMN_MONTHLY_LISTENS
                    HistoryRecord.TIME_ALL -> index = DBContract.DataEntry.COLUMN_ALL_TIME_LISTENS
                    else -> {
                        index = ""
                        // this will never happen
                    }
                }
                var listens = cursor.getInt(cursor.getColumnIndex(index))
                data.add(HistoryRecord(song, artist, "", listens, "Song"))
                cursor.moveToPrevious()

            }
        }
        cursor.close()
        return data
    }

    @Throws(SQLiteException::class)
    fun getArtists(timeframe: Int) : ArrayList<HistoryRecord>
    {
        var data = ArrayList<HistoryRecord>()
        val db = writableDatabase
        var cursor: Cursor? = null

        if (timeframe == HistoryRecord.TIME_DAILY) {
            try {
                cursor = db.rawQuery("select " + DBContract.DataEntry.COLUMN_ARTIST_NAME + ", "+ DBContract.DataEntry.COLUMN_DAILY_LISTENS + " from " + DBContract.DataEntry.TABLE_ARTIST_DATA
                        + " order by " + DBContract.DataEntry.COLUMN_DAILY_LISTENS + " desc"
                    , null)
            } catch(e: SQLiteException) {
                e.printStackTrace()
                Log.w("asdf", "Error retrieving daily artist listening")
            }
        } else if (timeframe == HistoryRecord.TIME_WEEKLY) {
            try {
                cursor = db.rawQuery("select " + DBContract.DataEntry.COLUMN_ARTIST_NAME + ", "+ DBContract.DataEntry.COLUMNN_WEEKLY_LISTENS + " from " + DBContract.DataEntry.TABLE_ARTIST_DATA
                        + " order by " + DBContract.DataEntry.COLUMNN_WEEKLY_LISTENS + " desc"
                    , null)
            } catch(e: SQLiteException) {
                e.printStackTrace()
                Log.w("asdf", "Error retrieving weekly artist listening")
            }
        } else if (timeframe == HistoryRecord.TIME_MONTHLY) {
            try {
                cursor = db.rawQuery("select " + DBContract.DataEntry.COLUMN_ARTIST_NAME + ", "+ DBContract.DataEntry.COLUMN_MONTHLY_LISTENS + " from " + DBContract.DataEntry.TABLE_ARTIST_DATA
                        + " order by " + DBContract.DataEntry.COLUMN_MONTHLY_LISTENS + " desc"
                    , null)
            } catch(e: SQLiteException) {
                e.printStackTrace()
                Log.w("asdf", "Error retrieving monthly artist listening")
            }
        } else if (timeframe == HistoryRecord.TIME_ALL) {
            try {
                cursor = db.rawQuery("select " + DBContract.DataEntry.COLUMN_ARTIST_NAME + ", "+ DBContract.DataEntry.COLUMN_ALL_TIME_LISTENS + " from " + DBContract.DataEntry.TABLE_ARTIST_DATA
                        + " order by " + DBContract.DataEntry.COLUMN_ALL_TIME_LISTENS + " desc"
                    , null)
            } catch(e: SQLiteException) {
                e.printStackTrace()
                Log.w("asdf", "Error retrieving all time artist listening")
            }
        } else {
            Log.w("asdf", "Error: Invalid code passed")
            cursor!!.close()
            return ArrayList<HistoryRecord>()
        }

        if (cursor!!.moveToLast()) {
            while (!cursor.isBeforeFirst) {
                var artist = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_ARTIST_NAME))
                var index: String
                when(timeframe) {
                    HistoryRecord.TIME_DAILY -> index = DBContract.DataEntry.COLUMN_DAILY_LISTENS
                    HistoryRecord.TIME_WEEKLY -> index = DBContract.DataEntry.COLUMNN_WEEKLY_LISTENS
                    HistoryRecord.TIME_MONTHLY -> index = DBContract.DataEntry.COLUMN_MONTHLY_LISTENS
                    HistoryRecord.TIME_ALL -> index = DBContract.DataEntry.COLUMN_ALL_TIME_LISTENS
                    else -> {
                        index = ""
                        // this will never happen
                    }
                }
                var listens = cursor.getInt(cursor.getColumnIndex(index))
                data.add(HistoryRecord("", artist, "", listens, "Artist"))
                cursor.moveToPrevious()

            }
        }
        cursor.close()
        return data
    }

    @Throws(SQLiteException::class)
    fun getAlbums(timeframe: Int) : ArrayList<HistoryRecord> {
        var data = ArrayList<HistoryRecord>()
        val db = writableDatabase
        var cursor: Cursor? = null

        if (timeframe == HistoryRecord.TIME_DAILY) {
            try {
                cursor = db.rawQuery("select " + DBContract.DataEntry.COLUMN_ALBUM_NAME + ", " + DBContract.DataEntry.COLUMN_ARTIST_NAME
                        + ", "+ DBContract.DataEntry.COLUMN_DAILY_LISTENS + " from " + DBContract.DataEntry.TABLE_ALBUM_DATA + " order by " + DBContract.DataEntry.COLUMN_DAILY_LISTENS + " desc"
                    , null)
            } catch(e: SQLiteException) {
                e.printStackTrace()
                Log.w("asdf", "Error retrieving daily album listening")
            }
        } else if (timeframe == HistoryRecord.TIME_WEEKLY) {
            try {
                cursor = db.rawQuery("select " + DBContract.DataEntry.COLUMN_ALBUM_NAME + ", " + DBContract.DataEntry.COLUMN_ARTIST_NAME
                        + ", "+ DBContract.DataEntry.COLUMNN_WEEKLY_LISTENS + " from " + DBContract.DataEntry.TABLE_ALBUM_DATA + " order by " + DBContract.DataEntry.COLUMNN_WEEKLY_LISTENS + " desc"
                    , null)
            } catch(e: SQLiteException) {
                e.printStackTrace()
                Log.w("asdf", "Error retrieving weekly album listening")
            }
        } else if (timeframe == HistoryRecord.TIME_MONTHLY) {
            try {
                cursor = db.rawQuery("select " + DBContract.DataEntry.COLUMN_ALBUM_NAME + ", " + DBContract.DataEntry.COLUMN_ARTIST_NAME
                        + ", "+ DBContract.DataEntry.COLUMN_MONTHLY_LISTENS + " from " + DBContract.DataEntry.TABLE_ALBUM_DATA + " order by " + DBContract.DataEntry.COLUMN_MONTHLY_LISTENS + " desc"
                    , null)
            } catch(e: SQLiteException) {
                e.printStackTrace()
                Log.w("asdf", "Error retrieving monthly album listening")
            }
        } else if (timeframe == HistoryRecord.TIME_ALL) {
            try {
                cursor = db.rawQuery("select " + DBContract.DataEntry.COLUMN_ALBUM_NAME + ", " + DBContract.DataEntry.COLUMN_ARTIST_NAME
                        + ", "+ DBContract.DataEntry.COLUMN_ALL_TIME_LISTENS + " from " + DBContract.DataEntry.TABLE_ALBUM_DATA + " order by " + DBContract.DataEntry.COLUMN_ALL_TIME_LISTENS + " desc"
                    , null)
            } catch(e: SQLiteException) {
                e.printStackTrace()
                Log.w("asdf", "Error retrieving all time album listening")
            }
        } else {
            Log.w("asdf", "Error: Invalid code passed")
            cursor!!.close()
            return ArrayList<HistoryRecord>()
        }

        if (cursor!!.moveToLast()) {
            while (!cursor.isBeforeFirst) {
                var album = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_ALBUM_NAME))
                var artist = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_ARTIST_NAME))
                var index: String
                when(timeframe) {
                    HistoryRecord.TIME_DAILY -> index = DBContract.DataEntry.COLUMN_DAILY_LISTENS
                    HistoryRecord.TIME_WEEKLY -> index = DBContract.DataEntry.COLUMNN_WEEKLY_LISTENS
                    HistoryRecord.TIME_MONTHLY -> index = DBContract.DataEntry.COLUMN_MONTHLY_LISTENS
                    HistoryRecord.TIME_ALL -> index = DBContract.DataEntry.COLUMN_ALL_TIME_LISTENS
                    else -> {
                        index = ""
                        // this will never happen
                    }
                }
                var listens = cursor.getInt(cursor.getColumnIndex(index))
                data.add(HistoryRecord("", artist, album, listens, "Album"))
                cursor.moveToPrevious()

            }
        }
        cursor.close()
        return data
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

    // TODO: DEPRECATING!!!
    @Throws(SQLiteConstraintException::class)
    fun showRecent():ArrayList<SpotifyPersonalData>{
        val data = ArrayList<SpotifyPersonalData>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.DataEntry.TABLE_NAME + " ORDER BY " + DBContract.DataEntry.COLUMN_RECORD_ID + " DESC LIMIT " + 10,null)
            Log.w("asdf", "show recent successful")
        }catch (e: SQLiteException) {
            // if table not yet present, create it
            Log.w("asdf", "${e.printStackTrace()}")
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


        if (cursor!!.moveToLast()) {
            while (cursor.isBeforeFirst == false) {
                recordid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(
                    DBContract.DataEntry.COLUMN_RECORD_ID)))
                userid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_USER_ID)))
                artistname = cursor.getString(cursor.getColumnIndex(DBContract.DataEntry.COLUMN_ARTIST_NAME))
                popularityscore = Integer.parseInt(cursor.getString(cursor.getColumnIndex(
                    DBContract.DataEntry.COLUMN_POPULARITY_SCORE)))
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