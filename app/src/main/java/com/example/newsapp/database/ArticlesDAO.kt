package com.example.newsapp.database

import androidx.room.*
import com.example.newsapp.models.Article

@Dao
interface ArticlesDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticles(articlesList: MutableList<ArticleEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticles(article: ArticleEntity)

    @Query("Select * from articles_table where saved = 'true'")
    fun getSavedArticles(): MutableList<ArticleEntity>

    @Query("Select * from articles_table")
    fun getCachedArticles(): MutableList<ArticleEntity>

    @Query( "Update articles_table set saved = :saved where url = :url")
    fun updateArticleSaveStatus(saved: Boolean, url: String)
}