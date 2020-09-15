package com.pareandroid.githubapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private var retrofit: Retrofit? = null

    private const val BASE_URL = "https://api.github.com/"


    val getApiServices: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            }
            return retrofit
        }
}