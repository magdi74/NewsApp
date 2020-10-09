package com.example.newsapp.apiclient

import com.example.newsapp.apicalls.ApiServiceInterface
import com.example.newsapp.tools.Constants
import com.example.newsapp.models.Article
import com.example.newsapp.models.ArticlesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NewsClient {
    var apiServices: ApiServiceInterface
    init{
        val  retrofit = Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiServices = retrofit.create(ApiServiceInterface::class.java)
    }
}