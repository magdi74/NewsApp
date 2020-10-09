package com.example.newsapp.database

import androidx.room.*
import com.example.newsapp.models.Article

@Dao
interface ArticlesDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticles(articlesList: MutableList<ArticleEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticles(article: ArticleEntity)

    @Query("Select * from articles_table where saved = :saveStatus")
    fun getSavedArticles(saveStatus: Boolean = true): MutableList<ArticleEntity>

    @Query("Select * from articles_table")
    fun getCachedArticles(): MutableList<ArticleEntity>

    @Update(entity = ArticleEntity::class)
    fun updateArticleSaveStatus(article: ArticleEntity)
}