package com.pareandroid.githubapp.api

import com.pareandroid.githubapp.model.SearchResponse
import com.pareandroid.githubapp.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("users")
    fun getApiUsers(): Call<List<User>>

    @GET("search/users")
    @Headers("Authorization: token 9e317b1288ab4a2339d0c1f3d93aacfc2b50d700")
    fun getSearchUser(@Query("q") query: String?): Call<SearchResponse>

    @GET("users/{username}")
    fun getDetail(
        @Path("username") username: String
    ): Call<User>

    @GET("users/{username}/{follow}")
    @Headers("Authorization: token 9e317b1288ab4a2339d0c1f3d93aacfc2b50d700")
    fun getDetailFollow(
        @Path("username") username: String,
        @Path("follow") follow: String
    ): Call<ArrayList<User>>
}