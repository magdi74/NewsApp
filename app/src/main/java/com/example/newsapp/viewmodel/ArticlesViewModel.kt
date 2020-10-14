package com.example.newsapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.apiclient.NewsRepository
import com.example.newsapp.database.ArticleEntity

class ArticlesViewModel(application: Application) : AndroidViewModel(application){
    init {
        // database is initialized whenever ViewModel is created.
        NewsRepository.initDataBase(application)
    }

    var mutableHeadlines: MutableLiveData<MutableList<ArticleEntity>> = MutableLiveData()
    var mutableSaved: MutableLiveData<MutableList<ArticleEntity>> = MutableLiveData()

    fun getNews(): LiveData<MutableList<ArticleEntity>> {
        return mutableHeadlines
    }

    fun callNews(page: Int){
        mutableHeadlines = NewsRepository.getNews(page)
    }

    fun getSavedArticles(): LiveData<MutableList<ArticleEntity>>{
        return mutableSaved
    }

    fun callSavedArticles(){
        mutableSaved = NewsRepository.getSavedArticles()
    }

    fun updateSaved(article: ArticleEntity){
        NewsRepository.updateArticles(article)
    }
}