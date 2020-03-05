package com.example.playback

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

import java.util.ArrayList

class AcctDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
{
    override fun onCreate(db: SQLiteDatabase)
    {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int)
    {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int)
    {
        onUpgrade(db, oldVersion, newVersion)
    }

    @Throws(SQLiteConstraintException::class)
    fun insert_user(user: Account): Boolean
    {
        val db = writableDatabase

        val values = ContentValues()
        values.put(AcctSchema.UserEntry.COLUMN_USER_ID, user.user_id)
        values.put(AcctSchema.UserEntry.COLUMN_USER_NAME, user.user_name)
        values.put(AcctSchema.UserEntry.COLUMN_PASSWORD, user.password)

        val new_row_id = db.insert(AcctSchema.UserEntry.TABLE_NAME, null, values)
        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun delete_user(user_id:String): Boolean
    {
        val db = writableDatabase

        val selection = AcctSchema.UserEntry.COLUMN_USER_ID + " LIKE ?"
        val selection_args = arrayOf(user_id)
        db.delete(AcctSchema.UserEntry.TABLE_NAME, selection, selection_args)
        return true
    }

    fun read_user(user_id: Int): ArrayList<Account>
    {
        val users = ArrayList<Account>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try
        {
            cursor = db.rawQuery("select * from " + AcctSchema.UserEntry.TABLE_NAME + " where " + AcctSchema.UserEntry.COLUMN_USER_ID + "='" + user_id
            + "'", null)
        } catch(e: SQLiteException)
        {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var user_name: String
        var password: String
        if (cursor!!.moveToFirst())
        {
            while (!cursor.isAfterLast)
            {
                user_name = cursor.getString(cursor.getColumnIndex(AcctSchema.UserEntry.COLUMN_USER_NAME))
                password = cursor.getString(cursor.getColumnIndex(AcctSchema.UserEntry.COLUMN_PASSWORD))

                users.add(Account(user_id, user_name, password))
                cursor.moveToNext()
            }
        }
        return users
    }

    fun read_all_users(): ArrayList<Account>
    {
        val users = ArrayList<Account>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try
        {
            cursor = db.rawQuery("select * from " + AcctSchema.UserEntry.TABLE_NAME, null)
        } catch(e: SQLiteException)
        {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var user_id: Int
        var user_name: String
        var password: String
        if (cursor!!.moveToFirst())
        {
            while(!cursor.isAfterLast) {
                user_id =
                    cursor.getInt(cursor.getColumnIndex(AcctSchema.UserEntry.COLUMN_USER_ID))
                user_name =
                    cursor.getString(cursor.getColumnIndex(AcctSchema.UserEntry.COLUMN_USER_NAME))
                password =
                    cursor.getString(cursor.getColumnIndex(AcctSchema.UserEntry.COLUMN_PASSWORD))
                users.add(Account(user_id, user_name, password))
                cursor.moveToNext()
            }
        }
        return users
    }

    // define the create and delete entries vals
    companion object
    {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "LocalStorage.db"

        private val SQL_CREATE_ENTRIES = "create table " + AcctSchema.UserEntry.TABLE_NAME +
                " (" + AcctSchema.UserEntry.COLUMN_USER_ID + " integer primary key," +
                AcctSchema.UserEntry.COLUMN_USER_NAME + " text," +
                AcctSchema.UserEntry.COLUMN_PASSWORD + " text)"

        private val SQL_DELETE_ENTRIES = "drop table if exists " + AcctSchema.UserEntry.TABLE_NAME
    }

}