package com.example.playback

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.content.ContentValues

class DBManager(context: Context, name: String?,
                factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, DATABASE_NAME,
        factory, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "PersonalSpotifyDB.db"
        const val tablePersonalData = "PersonalData"

        const val COLUMN_RecordID = "recordId"
        const val COLUMN_UserID = "userId"
        const val COLUMN_ArtistName = "artistName"
        const val COLUMN_PopularityScore = "popularityScore"
    }

    override fun onCreate(db: SQLiteDatabase) {
        /**SQL statement to create a table**/
        val createPersonalDataTABLE = ("CREATE TABLE " +
                tablePersonalData + "("
                + COLUMN_RecordID + " INTEGER PRIMARY KEY," +
                COLUMN_UserID + " INTEGER PRIMARY KEY,"
                + COLUMN_ArtistName + " TEXT," + COLUMN_PopularityScore + " INTEGER" + ")")
        db.execSQL(createPersonalDataTABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int,
                           newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $tablePersonalData")
        onCreate(db)
    }

    fun addData(data : SpotifyPersonalData) {
        val values = ContentValues()
        values.put(COLUMN_RecordID, data.recordId)
        values.put(COLUMN_UserID, data.userId)
        values.put(COLUMN_ArtistName, data.artistName)
        values.put(COLUMN_PopularityScore, data.popularityScore)

        val db = this.writableDatabase

        db.insert(tablePersonalData, null, values)
        db.close()
    }

    fun findData(user_id: Int): SpotifyPersonalData? {
        val query =
                "SELECT * FROM $tablePersonalData WHERE $COLUMN_UserID =  \"$user_id\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        var data: SpotifyPersonalData? = null

        if (cursor.moveToFirst()) {
            cursor.moveToFirst()

            val recordId = Integer.parseInt(cursor.getString(0))
            val userId = Integer.parseInt(cursor.getString(1))
            val artistName = cursor.getString(2)
            val popularityScore = Integer.parseInt(cursor.getString(3))

            data = SpotifyPersonalData(recordId, userId, artistName, popularityScore)
            cursor.close()
        }

        db.close()
        return data
    }

    fun deleteData(user_id: Int): Boolean {

        var deleted = false

        val query =
                "SELECT * FROM $tablePersonalData WHERE $COLUMN_UserID = \"$user_id\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            val userId = Integer.parseInt(cursor.getString(1))
            db.delete(tablePersonalData, "$COLUMN_UserID = ?",
                    arrayOf(user_id.toString()))
            cursor.close()
            deleted = true
        }
        db.close()
        return deleted
    }

    fun updateData(data:SpotifyPersonalData, record_id: Int): Boolean {

        var exied = false;
        val values = ContentValues()
        val query =
                "SELECT * FROM $tablePersonalData WHERE $COLUMN_RecordID = \"$record_id\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        if (!cursor.moveToFirst()) {
            values.put(COLUMN_RecordID, data.recordId)
            values.put(COLUMN_UserID, data.userId)
            values.put(COLUMN_ArtistName, data.artistName)
            values.put(COLUMN_PopularityScore, data.popularityScore)
            db.insert(tablePersonalData, null, values)
            exied = true
        }

        db.close()
        return exied;
    }
}