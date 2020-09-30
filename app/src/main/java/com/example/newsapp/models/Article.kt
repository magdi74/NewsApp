package com.example.newsapp.models

data class Article(
    var source: Source,
    var author: String,
    var title: String,
    var description: String,
    var url: String,
    var imageUrl: String,
    var publishedAt: String,
    var content: String
)