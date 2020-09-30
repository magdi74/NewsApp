package com.example.newsapp.apicalls

import com.example.newsapp.constants.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceInterface {
    @GET("/v2/top-headlines")
    fun getHeadlines(
        @Query("apikey") apiKey: String = Constants.API_KEY,
        @Query("language") country: String = "en"
    ) /*: Call<>*/

    @GET("/v2/everything")
    fun getEverything(
        @Query("apikey") apiKey: String = Constants.API_KEY,
        @Query("language") country: String = "en"
    ) /*: Call<> */
}