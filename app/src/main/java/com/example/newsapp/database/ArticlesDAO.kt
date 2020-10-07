package com.example.newsapp.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.newsapp.models.Article

interface ArticlesDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieList(movieList: MutableList<Article>)

    @Query("Select * from articles_table where saved = 'true'")
    fun getSavedArticles(): MutableList<ArticleEntity>

    @Query("Select * from articles_table")
    fun getCachedArticles(): MutableList<ArticleEntity>

    @Update
    fun updateArticleSaveStatus(article: Article)
}