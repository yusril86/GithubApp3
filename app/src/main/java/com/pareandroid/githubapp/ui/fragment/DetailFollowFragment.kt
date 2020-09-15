package com.pareandroid.githubapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pareandroid.githubapp.R
import com.pareandroid.githubapp.adapter.UserAdapter
import com.pareandroid.githubapp.api.ApiConfig
import com.pareandroid.githubapp.api.ApiInterface
import com.pareandroid.githubapp.model.User
import kotlinx.android.synthetic.main.fragment_detail_follow.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailFollowFragment : Fragment() {


    private var listUser = arrayListOf<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_follow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var username = "username"
        var tab = "tab"
        if (arguments != null) {
            username = arguments?.getString(ARG_USERNAME, "username") as String
            tab = arguments?.getString(ARG_TAB, "tab") as String
        }
        initRecyclerView()
        getUserFollow(username, tab)
    }

    companion object {
        private val ARG_USERNAME = "arg username"
        private val ARG_TAB = "arg tab"
        fun newInstance(username: String?, tab: String?) =
            DetailFollowFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERNAME, username)
                    putString(ARG_TAB, tab)
                }
            }
    }

    private fun initRecyclerView() {
        rv_fragment.layoutManager = LinearLayoutManager(activity)
        val listUserAdapter = UserAdapter(listUser)
        rv_fragment.adapter = listUserAdapter
        rv_fragment.setHasFixedSize(true)
    }

    private fun getUserFollow(username: String, follow: String) {
        val call = ApiConfig.getApiServices?.create(ApiInterface::class.java)
            ?.getDetailFollow(username, follow)
        call?.enqueue(object : Callback<ArrayList<User>> {
            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Toast.makeText(activity, "Gagal load data", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body() as ArrayList
                    for (list in data) {
                        listUser.add(list)
                    }
                    initRecyclerView()
                }
            }
        })
    }
}