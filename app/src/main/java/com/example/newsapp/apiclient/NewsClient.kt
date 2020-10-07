package com.example.newsapp.apiclient

import com.example.newsapp.apicalls.ApiServiceInterface
import com.example.newsapp.constants.Constants
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

    fun fetchHeadlines(
        page: Int,
        onSuccess: (headlines: MutableList<Article>) -> Unit,
        onError: () -> Unit
    ){
        apiServices.getTopHeadlines(page = page)
            .enqueue(object: Callback<ArticlesResponse>{
                override fun onResponse(
                    call: Call<ArticlesResponse>,
                    response: Response<ArticlesResponse>
                ) {
                    if(response.isSuccessful){
                        if(response.body() != null)
                            onSuccess.invoke(response.body()!!.articles)
                    }
                    else onError.invoke()
                }

                override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) = onError.invoke()
            })
    }
}