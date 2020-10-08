package com.pareandroid.githubapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.pareandroid.githubapp.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME_LOGIN
import com.pareandroid.githubapp.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.pareandroid.githubapp.db.DatabaseContract.UserColumns.Companion._ID
import java.sql.SQLException

class UserHelper (context :Context){

    companion object{
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private var INSTANCE : UserHelper? = null
        private lateinit var database : SQLiteDatabase

        fun getInstance(context: Context):UserHelper =
            INSTANCE?: synchronized(this){
                INSTANCE?: UserHelper(context)
            }
    }

    init {
        databaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open (){
        database = databaseHelper.writableDatabase
    }

    fun isOpen():Boolean{
        return try {
            database.isOpen
        }catch (e:SQLException){
            false
        }
    }

    fun close(){
        databaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun queryAll():Cursor{
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }

    fun queryByUserLogin(login :String):Cursor{
        return database.query(
            DATABASE_TABLE,
            null,
            "$COLUMN_NAME_LOGIN = ?",
            arrayOf(login),
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?):Long{
        return database.insert(DATABASE_TABLE,null,values)
    }

    fun deleteByUserLogin(id:String):Int{
        return database.delete(DATABASE_TABLE,"$COLUMN_NAME_LOGIN = '$id'",null)
    }
}