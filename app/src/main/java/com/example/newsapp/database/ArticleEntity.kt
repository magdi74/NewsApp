package com.example.newsapp.database

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsapp.models.Source

@Entity(tableName = "articles_table")
class ArticleEntity (
    var source: Source?,
    var author: String?,
    var title: String?,
    var description: String?,
    @PrimaryKey var url: String,
    var imageUrl: String?,
    var publishedAt: String?,
    var content: String?,
    var saved: Boolean?
    )