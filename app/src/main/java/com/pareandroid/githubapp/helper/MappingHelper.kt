package com.pareandroid.githubapp.helper

import android.database.Cursor
import com.pareandroid.githubapp.db.DatabaseContract
import com.pareandroid.githubapp.model.User

object MappingHelper {
    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<User> {
        val favoriteUserList = ArrayList<User>()

        userCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val login =
                    getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_NAME_LOGIN))
                val avatarUrl =
                    getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_NAME_AVATAR_URL))
                favoriteUserList.add(
                    User(
                        login,
                        null,
                        avatarUrl,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        id
                    )
                )
            }
        }
        return favoriteUserList
    }
}