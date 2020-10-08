package com.pareandroid.githubapp.widget

import android.content.Intent
import android.widget.RemoteViewsService

class StackWidgetServices : RemoteViewsService() {
    override fun onGetViewFactory(p0: Intent?): RemoteViewsFactory = StackRemoteViewsFactory(this.applicationContext)
}