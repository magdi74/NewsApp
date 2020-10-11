package com.example.newsapp.viewmodel

import android.app.Application
import android.content.Context
import android.os.AsyncTask
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
     //   saveasync().execute(article)

         NewsRepository.updateArticles(article)
    }
/*
    inner class saveasync : AsyncTask<ArticleEntity,Unit,Unit> (){
        override fun doInBackground(vararg params: ArticleEntity?) {
            NewsRepository.updateArticles(params[0]!!)
           // updateSaved(params[0]!!)
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            callSavedArticles()
            callNews()

        }


    }
*/
}