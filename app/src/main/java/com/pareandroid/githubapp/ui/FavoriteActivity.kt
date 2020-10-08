package com.pareandroid.githubapp.ui

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.pareandroid.githubapp.R
import com.pareandroid.githubapp.adapter.FavoriteAdapter
import com.pareandroid.githubapp.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.pareandroid.githubapp.db.UserHelper
import com.pareandroid.githubapp.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_LOGIN = "extra_login"
        const val EXTRA_AVATAR_URL = "extra_avatar_url"
    }

    private lateinit var favoriteUserHelper: UserHelper
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        supportActionBar?.apply {
            title = "Favorite User"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        showFavoriteUser()

        favoriteUserHelper = UserHelper.getInstance(applicationContext)
        favoriteUserHelper.open()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object: ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadFavoriteUserAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI,true,myObserver)
    }


    private fun showFavoriteUser() {
        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.setHasFixedSize(true)
        adapter = FavoriteAdapter()
        rv_favorite.adapter = adapter
    }

    private fun loadFavoriteUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBarFavorite.visibility = View.VISIBLE
            val deferredFavorite = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressBarFavorite.visibility = View.GONE
            val mFavorite = deferredFavorite.await()
            if (mFavorite.size > 0) {
                adapter.listUser = mFavorite
            } else {
                adapter.listUser = ArrayList()
                Snackbar.make(rv_favorite,getString(R.string.no_data_favorite),Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.apply {
            putParcelableArrayList(EXTRA_LOGIN,adapter.listUser)
            putParcelableArrayList(EXTRA_AVATAR_URL,adapter.listUser)
        }
    }

    override fun onResume() {
        super.onResume()
        showFavoriteUser()
        loadFavoriteUserAsync()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}