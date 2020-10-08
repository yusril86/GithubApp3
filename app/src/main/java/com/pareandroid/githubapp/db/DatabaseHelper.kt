package com.pareandroid.githubapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.pareandroid.githubapp.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME_AVATAR_URL
import com.pareandroid.githubapp.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME_LOGIN
import com.pareandroid.githubapp.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.pareandroid.githubapp.db.DatabaseContract.UserColumns.Companion._ID

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "dbFavoriteUser"
        private const val DATABASE_VERSION = 1

        private val SQL_CREATE_TABLE = "CREATE TABLE $TABLE_NAME" +
                "($_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " $COLUMN_NAME_LOGIN TEXT NOT NULL UNIQUE," +
                " $COLUMN_NAME_AVATAR_URL TEXT NOT NULL)"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(p0)
    }
}