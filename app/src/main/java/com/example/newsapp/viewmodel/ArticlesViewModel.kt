package com.example.newsapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.apiclient.NewsRepository
import com.example.newsapp.database.ArticleEntity

class ArticlesViewModel: ViewModel() {
    var headlinesMutableLiveData: MutableLiveData<MutableList<ArticleEntity>> = MutableLiveData()
    var savedMutableLiveData: MutableLiveData<MutableList<ArticleEntity>> = MutableLiveData()

    fun getHeadlines(context: Context){
        fun onComplete(mutableList: MutableList<ArticleEntity>) {
            headlinesMutableLiveData.postValue((mutableList))
        }

        fun onIncomplete(mutableList: MutableList<ArticleEntity>) {
            headlinesMutableLiveData.postValue(mutableList)
        }
        NewsRepository.getAllArticles(context, ::onComplete, ::onIncomplete)
    }

    fun getSavedHeadlines(context: Context){
        fun onLoaded(mutableList: MutableList<ArticleEntity>) {
            savedMutableLiveData.postValue(mutableList)
        }
        NewsRepository.getSavedArticles(context, ::onLoaded)

    }

}