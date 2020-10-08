package com.pareandroid.githubapp.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.widget.Toast
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.pareandroid.githubapp.R
import com.pareandroid.githubapp.db.UserHelper
import com.pareandroid.githubapp.helper.MappingHelper

internal class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {
    private lateinit var userHelper: UserHelper
    private val widgetItem = ArrayList<Bitmap>()

    override fun onCreate() {
        TODO("Not yet implemented")
    }

    override fun onDataSetChanged() {
        userHelper = UserHelper.getInstance(mContext)
        if (!userHelper.isOpen()) userHelper.open()

        val identifyToken = Binder.clearCallingIdentity()
        Binder.restoreCallingIdentity(identifyToken)

        try {
            val cursorSearch = userHelper.queryAll()
            val cursor = MappingHelper.mapCursorToArrayList(cursorSearch)
            if (cursor.size > 0) {
                widgetItem.clear()
                for (i in 0 until cursor.size) {
                    try {
                        val avatar = cursor[i].avatar.toString()
                        val bitmap = Glide.with(mContext)
                            .asBitmap()
                            .load(avatar)
                            .submit()
                            .get()
                        widgetItem.add(bitmap)
                    } catch (e: Exception) {
                        widgetItem.add(
                            BitmapFactory.decodeResource(
                                mContext.resources,
                                R.drawable.ic_launcher_foreground
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(mContext, "$e", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getViewAt(p0: Int): RemoteViews {
        val remote = RemoteViews(mContext.packageName, R.layout.widget_item)
        remote.setImageViewBitmap(R.id.imageView, widgetItem[p0])

        val extras = bundleOf(GithubAppWidget.EXTRA_ITEM to p0)

        val fillIntent = Intent()
        fillIntent.putExtras(extras)

        remote.setOnClickFillInIntent(R.id.imageView, fillIntent)
        return remote

    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(p0: Int): Long = 0

    override fun hasStableIds(): Boolean = false

    override fun getCount(): Int = widgetItem.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {
    }
}