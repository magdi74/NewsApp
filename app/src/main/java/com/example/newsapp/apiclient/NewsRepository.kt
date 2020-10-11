package com.example.newsapp.apiclient

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.database.ArticleEntity
import com.example.newsapp.database.ArticlesDAO
import com.example.newsapp.database.ArticlesDatabase
import com.example.newsapp.models.Article
import com.example.newsapp.models.ArticlesResponse
import com.example.newsapp.models.Source
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object NewsRepository {
    private var apiClient = NewsClient.apiServices

    private lateinit var appDatabase: ArticlesDatabase

    var articlesList: MutableList<ArticleEntity> = mutableListOf()

    fun initDataBase(context: Context) {
        appDatabase = ArticlesDatabase.getDatabaseInstance(context)
    }

    fun getNews(pagenum: Int = 1): MutableLiveData<MutableList<ArticleEntity>> {
        val newsListLiveData: MutableLiveData<MutableList<ArticleEntity>> = MutableLiveData()

        if (articlesList.isNotEmpty()) {
           newsListLiveData.postValue(articlesList)
            return newsListLiveData
        }

        apiClient.getTopHeadlines(page = pagenum).enqueue(object : Callback<ArticlesResponse> {

            override fun onResponse(call: Call<ArticlesResponse>, response: Response<ArticlesResponse>) {

                if (response.isSuccessful) {

                    var remoteList: MutableList<Article> = response.body()?.articles ?: mutableListOf()

                    var apiEntity = toEntity(remoteList)

                    articlesList.addAll(apiEntity)

                    var saved = setSavedStatus(articlesList,
                        appDatabase.ArticlesDao().getCachedArticles().filter{it.saved == true}.distinct().toMutableList())
                        .distinct().toMutableList()

                    appDatabase.ArticlesDao().insertArticles(saved)

                    newsListLiveData.postValue(saved)
                } else {
                    val savedNews = appDatabase.ArticlesDao().getCachedArticles()
                    newsListLiveData.postValue(savedNews)
                }
            }

            override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {

                val savedNews = appDatabase.ArticlesDao().getCachedArticles()
                newsListLiveData.postValue(savedNews)
            }

        })

        return newsListLiveData
    }
// changes a List of Acticles to List of ArticleEntity
    private fun toEntity(modelList: MutableList<Article>): MutableList<ArticleEntity>{
        var entityList: MutableList<ArticleEntity> = mutableListOf()
        for( i in 0 until modelList.size){
            var model = modelList[i]
            entityList.add(
                ArticleEntity(
                    source = Source(model.source.id, model.source.name),
                    author = model.author,
                    title = model.title,
                    description = model.description,
                    url = model.url,
                    imageUrl = model.imageUrl,
                    publishedAt = model.publishedAt,
                    content = model.content,
                    saved = false
                )
            )
        }
        return entityList
    }
    // get Saved Articles Function
    fun getSavedArticles(): MutableLiveData<MutableList<ArticleEntity>>{

        val newsListLiveData: MutableLiveData<MutableList<ArticleEntity>> = MutableLiveData()

        var savedList: MutableList<ArticleEntity> = mutableListOf()
        savedList = appDatabase.ArticlesDao().getCachedArticles().filter{it.saved == true}.distinct().toMutableList()

        if (savedList.isNotEmpty()) {
            Log.d("saved","Got Cached")
            newsListLiveData.postValue(savedList)
        }
        else{
            Log.d("saved","Empty")
            newsListLiveData.postValue(savedList)
        }
        return newsListLiveData

    }
    // Updates the Saved Articles
    fun updateArticles(article: ArticleEntity){
        appDatabase.ArticlesDao().updateArticleSaveStatus(article)
        appDatabase.ArticlesDao().insertArticles(article)
    }

    private fun setSavedStatus(apiEntities: MutableList<ArticleEntity>, saved: MutableList<ArticleEntity>): MutableList<ArticleEntity>{
        var articleEntities: MutableList<ArticleEntity> = mutableListOf()

        for(i in 0 until apiEntities.size){
            for(j in 0 until saved.size){
                if(apiEntities[i].url==saved[j].url){
                    articleEntities.add(saved[j])
                }
                else{
                    articleEntities.add(apiEntities[i])
                }
            }
        }
        return articleEntities
    }
}
