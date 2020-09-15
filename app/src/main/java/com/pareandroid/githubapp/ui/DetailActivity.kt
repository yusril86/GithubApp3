package com.pareandroid.githubapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pareandroid.githubapp.R
import com.pareandroid.githubapp.adapter.PagerFollowAdapter
import com.pareandroid.githubapp.api.ApiConfig
import com.pareandroid.githubapp.api.ApiInterface
import com.pareandroid.githubapp.model.User
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private lateinit var pagerAdapter: PagerFollowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        actionBar()
        val username: String = intent.getStringExtra(EXTRA_USER)
        getDetailUser(username)
        initSectionPager()
        pagerAdapter.username = username
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
}