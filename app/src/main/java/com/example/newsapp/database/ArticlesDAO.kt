package com.example.newsapp.database

import androidx.room.*
import com.example.newsapp.models.Article

@Dao
interface ArticlesDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticles(articlesList: MutableList<ArticleEntity>)

    @Query("Select * from articles_table where saved = 'true'")
    fun getSavedArticles(): MutableList<ArticleEntity>

    @Query("Select * from articles_table")
    fun getCachedArticles(): MutableList<ArticleEntity>

    @Update()
    fun updateArticleSaveStatus(article: ArticleEntity)
}