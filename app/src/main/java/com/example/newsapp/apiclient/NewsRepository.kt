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
//Handle fresh install and no network
object NewsRepository {
    private var apiClient = NewsClient.apiServices

    private lateinit var database: ArticlesDAO

    var articlesList: MutableList<ArticleEntity> = mutableListOf()

    fun getAllArticles(context: Context,
    onSuccess: (entities: MutableList<ArticleEntity>) ->  Unit,
    onFailure: (entities: MutableList<ArticleEntity>) -> Unit) {

        var articlesMutableLiveData: MutableLiveData<MutableList<ArticleEntity>> = MutableLiveData()

        if(articlesList.isNotEmpty()){
            //articlesMutableLiveData.postValue(articlesList)
            onSuccess.invoke(articlesList)
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

                    onSuccess.invoke(dataset)

                 //   articlesMutableLiveData.postValue(dataset)
                }
                else{
                    //articlesMutableLiveData.postValue(database.getCachedArticles())
                    onSuccess.invoke(database.getCachedArticles())
                }
            }

            override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {
                //articlesMutableLiveData.postValue(database.getCachedArticles())
                onFailure(database.getCachedArticles())
            }
        })
    }

    fun getSavedArticles(context: Context, onLoaded: (savedArticles: MutableList<ArticleEntity>) -> Unit){
        database = ArticlesDatabase.getDatabaseInstance(context).ArticlesDao()
        onLoaded.invoke(database.getSavedArticles())
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