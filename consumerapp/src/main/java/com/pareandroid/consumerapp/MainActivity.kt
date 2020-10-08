package com.pareandroid.consumerapp

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.pareandroid.consumerapp.adapter.FavoriteAdapter
import com.pareandroid.consumerapp.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.pareandroid.githubapp.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadFavoriteUserAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI,true,myObserver)

        showFavoriteUser()
        loadFavoriteUserAsync()
    }

    private fun showFavoriteUser() {
        rv_main.layoutManager = LinearLayoutManager(this)
        rv_main.setHasFixedSize(true)
        adapter = FavoriteAdapter()
        rv_main.adapter = adapter
    }

    private fun loadFavoriteUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredFavorite = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val mFavorite = deferredFavorite.await()
            if (mFavorite.size > 0) {
                adapter.listUser = mFavorite
            } else {
                adapter.listUser = ArrayList()
                Snackbar.make(rv_main,getString(R.string.no_data_favorite), Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        showFavoriteUser()
        loadFavoriteUserAsync()
    }
}
