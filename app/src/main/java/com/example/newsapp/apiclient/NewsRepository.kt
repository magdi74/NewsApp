package com.example.newsapp.apiclient

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.database.ArticleEntity
import com.example.newsapp.database.ArticlesDAO
import com.example.newsapp.database.ArticlesDatabase
import com.example.newsapp.models.Article
import com.example.newsapp.models.ArticlesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object NewsRepository {
    private var apiClient = NewsClient.apiServices

    private lateinit var database: ArticlesDAO

    var articlesList: MutableList<ArticleEntity> = mutableListOf()

    fun getAllArticles(context: Context): LiveData<MutableList<ArticleEntity>> {

        var articlesMutableLiveData: MutableLiveData<MutableList<ArticleEntity>> = MutableLiveData()

        if(articlesList.isNotEmpty()){
            articlesMutableLiveData.postValue(articlesList)
            return articlesMutableLiveData
        }

        database = ArticlesDatabase.getDatabaseInstance(context).ArticlesDao()

        apiClient.getTopHeadlines().enqueue(object: Callback<ArticlesResponse>{
            override fun onResponse(
                call: Call<ArticlesResponse>,
                response: Response<ArticlesResponse>
            ) {
                if(response.isSuccessful){
                    var remoteList: MutableList<Article> = response.body()?.articles ?: mutableListOf()

                    var apiEntity = toEntity(remoteList)

                    var savedFromDatabase = database.getSavedArticles()

                    var dataset = setSavedStatus(apiEntity,savedFromDatabase)

                    database.insertArticles(dataset)

                    articlesMutableLiveData.postValue(dataset)
                }
                else{
                    articlesMutableLiveData.postValue(database.getCachedArticles())
                }
            }

            override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {
                articlesMutableLiveData.postValue(database.getCachedArticles())
            }
        })

        return articlesMutableLiveData
    }

    fun getSavedArticles(context: Context): LiveData<MutableList<ArticleEntity>>{
        database = ArticlesDatabase.getDatabaseInstance(context).ArticlesDao()

        var savedMutableLiveData: MutableLiveData<MutableList<ArticleEntity>> = MutableLiveData()

        savedMutableLiveData.postValue(database.getSavedArticles())

        return savedMutableLiveData
    }

    fun updateSavedArticles(context: Context, article: ArticleEntity){
        database = ArticlesDatabase.getDatabaseInstance(context).ArticlesDao()
        database.updateArticleSaveStatus(article)
    }

    private fun setSavedStatus(apiEntities: MutableList<ArticleEntity>, saved: MutableList<ArticleEntity>): MutableList<ArticleEntity>{
        var articleEntities: MutableList<ArticleEntity> = mutableListOf()
    //mmkn yedrab error hena
        for(i in 0 until apiEntities.size){
            for(j in 0 until saved.size){
                if(apiEntities[i].url==saved[i].url){
                    articleEntities.add(saved[i])
                }
                else{
                   articleEntities.add(apiEntities[i])
                }
            }
        }
        return articleEntities
    }

    private fun toEntity(modelList: MutableList<Article>): MutableList<ArticleEntity>{
        var entityList: MutableList<ArticleEntity> = mutableListOf()
        for( i in 0 until modelList.size){
            var model = modelList[i]
            entityList.add(
                ArticleEntity(
                    source = model.source,
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
}