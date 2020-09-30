package com.example.newsapp.apicalls

import com.example.newsapp.constants.Constants
import com.example.newsapp.models.ArticlesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceInterface {
    @GET("v2/top-headlines")
    fun getTopHeadlines(
        @Query("apikey") apiKey: String = Constants.API_KEY,
        @Query("language") language: String = "en",
        @Query("page") page: Int = 1
    ) : Call<ArticlesResponse>

    @GET("v2/everything")
    fun getEverything(
        @Query("apikey") apiKey: String = Constants.API_KEY,
        @Query("language") country: String = "en",
        @Query("page") page: Int = 1
    ) : Call<ArticlesResponse>
}