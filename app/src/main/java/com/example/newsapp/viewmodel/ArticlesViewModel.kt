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



}