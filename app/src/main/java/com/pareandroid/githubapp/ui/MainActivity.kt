package com.pareandroid.githubapp.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.pareandroid.githubapp.R
import com.pareandroid.githubapp.adapter.UserAdapter
import com.pareandroid.githubapp.api.ApiConfig
import com.pareandroid.githubapp.api.ApiInterface
import com.pareandroid.githubapp.model.SearchResponse
import com.pareandroid.githubapp.model.User
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private var githubUser = arrayListOf<User>()
    private lateinit var adapaterUser: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()
        getUser()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    recycler_view.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    githubUser.clear()
                    getSearchingUser(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun initRecyclerView() {
        recycler_view.layoutManager = LinearLayoutManager(this)
        adapaterUser = UserAdapter(githubUser)
        recycler_view.adapter = adapaterUser
        recycler_view.setHasFixedSize(true)
    }

    fun getUser() {
        val call = ApiConfig.getApiServices?.create(ApiInterface::class.java)
        call?.getApiUsers()?.enqueue(object : Callback<List<User>> {
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Gagal Load Data", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
                progressBar.visibility = View.GONE
            }

            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val listdata = response.body()
                    adapaterUser.setData(listdata as ArrayList<User>)
                    progressBar.visibility = View.GONE
                    Log.d("hasil", "jumlah ${listdata.size}")
                }
            }
        })
    }

    fun getSearchingUser(username: String?) {
        val call = ApiConfig.getApiServices?.create(ApiInterface::class.java)
        call?.getSearchUser(username)?.enqueue(object : Callback<SearchResponse> {
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Gagal Load Data", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
                progressBar.visibility = View.GONE
            }

            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                try {
                    val dataArray = response.body()?.items as ArrayList<User>
                    for (data in dataArray) {
                        githubUser.add(data)
                    }
                    recycler_view.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    initRecyclerView()
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.stackTrace
                }
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.action_language) {
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

}