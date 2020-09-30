package com.example.newsapp.models

import com.google.gson.annotations.SerializedName

data class ArticlesResponse(
    @SerializedName("articles") var articles: MutableList<Article>,
    @SerializedName("page") var currentPage: Int,
    @SerializedName("status") var status: String
)