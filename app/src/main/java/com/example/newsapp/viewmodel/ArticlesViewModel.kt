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



    /*
    fun getHeadlines(context: Context){
        fun onComplete(mutableList: MutableList<ArticleEntity>) {
            headlinesMutableLiveData.postValue((mutableList))
        }

        fun onIncomplete(mutableList: MutableList<ArticleEntity>) {
            headlinesMutableLiveData.postValue(mutableList)
        }
        NewsRepository.getAllArticles(context)
    }

    fun getSavedHeadlines(context: Context){
        fun onLoaded(mutableList: MutableList<ArticleEntity>) {
            savedMutableLiveData.postValue(mutableList)
        }
        NewsRepository.getSavedArticles(context, ::onLoaded)

    }

    fun updateArticle(context: Context, article: ArticleEntity){
        NewsRepository.updateSavedArticles(context, article)
    }
*/
}