package com.pareandroid.githubapp.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.pareandroid.githubapp.db.DatabaseContract.AUTHORITY
import com.pareandroid.githubapp.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.pareandroid.githubapp.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.pareandroid.githubapp.db.UserHelper

class UserProvider : ContentProvider() {

    companion object {
        private const val USER = 1
        private const val USER_ID = 2
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var userHelper: UserHelper
    }

    init {
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, USER)
        uriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", USER_ID)
    }

    override fun onCreate(): Boolean {
        userHelper = UserHelper.getInstance(context as Context)
        userHelper.open()
        return true
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            USER -> userHelper.queryAll()
            USER_ID -> userHelper.queryByUserLogin(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val add: Long? = when (USER) {
            uriMatcher.match(uri) -> values?.let { userHelper.insert(it) }
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI,null)
        return Uri.parse("$CONTENT_URI/$add")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted : Int = when(USER_ID){
            uriMatcher.match(uri)-> userHelper.deleteByUserLogin(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI,null)
        return deleted
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }
}
