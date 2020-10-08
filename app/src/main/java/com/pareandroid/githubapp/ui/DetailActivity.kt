package com.pareandroid.githubapp.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pareandroid.githubapp.R
import com.pareandroid.githubapp.adapter.PagerFollowAdapter
import com.pareandroid.githubapp.api.ApiConfig
import com.pareandroid.githubapp.api.ApiInterface
import com.pareandroid.githubapp.db.DatabaseContract
import com.pareandroid.githubapp.db.UserHelper
import com.pareandroid.githubapp.model.User
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.SQLException

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_AVATAR_URL = "extra_avatar_url"
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private lateinit var pagerAdapter: PagerFollowAdapter
    private lateinit var userHelper: UserHelper
    private var isFavorite: Boolean = false
    private lateinit var menuItem: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        actionBar()
        val username: String? = intent.getStringExtra(EXTRA_USER)
        getDetailUser(username)
        initSectionPager()
        pagerAdapter.username = username

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        favoriteState()
    }

    private fun getDetailUser(username: String?) {
        val call = username.let {
            ApiConfig.getApiServices?.create(ApiInterface::class.java)?.getDetail(it!!)
        }
        call?.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "gagal load data", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
                progressBar.visibility = View.INVISIBLE
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                try {

                    val data = response.body()

                    Glide.with(this@DetailActivity)
                        .load(data?.avatar)
                        .apply(RequestOptions())
                        .into(img_detail_photo)

                    tv_detail_fullname.text = if (data?.name != null) data.name else "-"
                    tv_detail_username.text = if (data?.username != null) data.username else "-"
                    tv_detail_company.text = if (data?.company != null) data.company else "-"
                    tv_detail_location.text = if (data?.location != null) data.location else "-"
                    tv_detail_repo.text = if (data?.repository != null) data.repository else "-"
                    tv_detail_followers.text = if (data?.follower != null) data.follower else "-"
                    tv_detail_following.text = if (data?.following != null) data.following else "-"
                    progressBarDetail.visibility = View.INVISIBLE
                } catch (e: Exception) {
                    Toast.makeText(this@DetailActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
        })
    }

    private fun initSectionPager() {
        pagerAdapter = PagerFollowAdapter(this, supportFragmentManager)
        view_pager.adapter = pagerAdapter
        tab_layout.setupWithViewPager(view_pager)
    }

    private fun actionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Detail User"
        }
    }

    private fun favoriteState() {
        val username = intent?.getStringExtra(EXTRA_USER).toString()
        val result = userHelper.queryByUserLogin(username)
        val favorite = (1..result.count).map {
            result.apply {
                moveToNext()
                getInt(result.getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_NAME_LOGIN))
            }
        }
        if (favorite.isNotEmpty()) isFavorite = true
    }

    private fun addFavorite() {
        try {
            val username = intent?.getStringExtra(EXTRA_USER).toString()
            val avatar = intent?.getStringExtra(EXTRA_AVATAR_URL).toString()

            val values = ContentValues().apply {
                put(DatabaseContract.UserColumns.COLUMN_NAME_LOGIN, username)
                put(DatabaseContract.UserColumns.COLUMN_NAME_AVATAR_URL, avatar)
            }
            userHelper.insert(values)
            Toast.makeText(this, getString(R.string.toast_favorite_add), Toast.LENGTH_SHORT).show()
            Log.d("Menambahkan...", values.toString())
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    private fun deleteFavorite() {
        try {
            val username = intent?.getStringExtra(EXTRA_USER).toString()
            val result = userHelper.deleteByUserLogin(username)
            Toast.makeText(this, getString(R.string.toast_deleted_favorite), Toast.LENGTH_SHORT)
                .show()
            Log.d("deleted...", result.toString())
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    private fun setFavorite() {
        if (isFavorite) {
            menuItem.getItem(1)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite)
        } else {
        menuItem.getItem(1)?.icon = ContextCompat.getDrawable(this,R.drawable.ic_favorite_border)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_language){
            val intentLanguage = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intentLanguage)
        }
        if (item.itemId == R.id.favorite_user) {
            if (isFavorite) deleteFavorite() else addFavorite()
            isFavorite = !isFavorite
            setFavorite()
        }
        return super.onOptionsItemSelected(item)
    }
}