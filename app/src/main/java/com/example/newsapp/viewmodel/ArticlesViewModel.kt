package com.example.newsapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.apiclient.NewsRepository
import com.example.newsapp.database.ArticleEntity

class ArticlesViewModel(application: Application) : AndroidViewModel(application){
    init {
        // Ensue database is initialized whenever ViewModel is created.
        NewsRepository.initDataBase(application)
    }

    fun getNews(): LiveData<MutableList<ArticleEntity>> {
        return NewsRepository.getNews()
    }

    fun getSavedArticles(): LiveData<MutableList<ArticleEntity>>{
        return NewsRepository.getSavedArticles()
    }

    fun updateSaved(){
    }


}