@file:Suppress("UNCHECKED_CAST")

package com.example.newsapp

import android.content.ClipData
import android.content.ClipboardManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.newsapp.adapters.HeadlinesAdapter
import com.example.newsapp.adapters.SavedItemsAdapter
import com.example.newsapp.database.ArticleEntity
import com.example.newsapp.viewmodel.ArticlesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_headlines.*
import kotlinx.android.synthetic.main.fragment_item_details.*
import kotlinx.android.synthetic.main.fragment_saved_items.*

var saveState = 0

class MainActivity :  AppCompatActivity() , HeadlinesAdapter.HeadlineListener, SavedItemsAdapter.SavedItemsListener {
    lateinit var llm: LinearLayoutManager

    var currentPageNumber = 1

    private val mainViewModel: ArticlesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Initialize headlines recyclerView
        var headlinesAdapter = HeadlinesAdapter(mutableListOf(), this)
        rv_headlines.adapter = headlinesAdapter
        rv_headlines.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        mainViewModel.getNews()
            .observe(this, Observer {
                //rv_headlines.adapter = HeadlinesAdapter(it as MutableList<ArticleEntity>?, this)
                headlinesAdapter.appendNews(it)
                attachonScrollListner()
            })

        //Initialize savedAdapter

        var savedAdapter = SavedItemsAdapter(mutableListOf(), this)

        saved_rv.adapter = savedAdapter
        saved_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        mainViewModel.getSavedArticles()
            .observe(this, Observer{
                savedAdapter.appendNews(it)
            })


        bottomNavMenu!!.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.headlinesFragment -> {
                    mainViewModel.getNews()
                    var fragManager = supportFragmentManager
                    fragManager.beginTransaction().show(headLinesFrag).hide(savedFrag).hide(detailsFrag).commit()

                    true
                }
                R.id.savedItemsFragment -> {
                    mainViewModel.getSavedArticles()

                    var llm_saved: LinearLayoutManager =
                        LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
//3ayz amsa8o
                    saved_rv.layoutManager = llm_saved
                    var fragManager = supportFragmentManager
                    fragManager.beginTransaction().show(savedFrag).hide(headLinesFrag).hide(detailsFrag).commit()
                    true
                }
                else -> false
            }

        }
    }

    private fun attachonScrollListner() {
        rv_headlines.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalArticles = llm.itemCount
                val visibleArticles = llm.childCount
                val firstVisibleArticle = llm.findLastVisibleItemPosition()

                if (firstVisibleArticle + visibleArticles >= totalArticles / 2) {
                    rv_headlines.removeOnScrollListener(this)
                    currentPageNumber++
                    mainViewModel.getNews()
                }
            }
        })

    }
    override fun headlineClicked(article: ArticleEntity,position: Int) {
      //  articleMain= article
        headline_details.text = article.title
        description.text = article.description
        article_date_details.text = article.publishedAt
        article_source_details.text = article.source!!.name
        Glide.with(this).load(article.imageUrl).transform(CenterCrop()).into(banner)
        if(article.saved==true)
        {
            detailsFrag.btnSave.setImageResource(R.drawable.ic_saved)
            saveState=1
        }
        else
        {
            detailsFrag.btnSave.setImageResource(R.drawable.ic_unsaved)
            saveState=0
        }
        var fragment : FragmentManager
        fragment = supportFragmentManager
        fragment.beginTransaction().show(detailsFrag).hide(headLinesFrag).hide(savedFrag).commit()

    }


    fun copyText(view: View){
        lateinit var article: ArticleEntity
        copyToClipboard(article.url.toString())
        Toast.makeText(this, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    fun copyToClipboard(text:CharSequence){
        val clipboard: ClipboardManager =getSystemService(CLIPBOARD_SERVICE)  as ClipboardManager
        val clip:ClipData = ClipData.newPlainText("Link copied to Clipboard",text)
        clipboard?.setPrimaryClip(clip)

    }

    override fun savedItemsClicked(article: ArticleEntity) {
      //  articleMain= article
        mainViewModel.getSavedArticles()
        headline_details.text = article.title
        description.text = article.description
        article_date_details.text = article.publishedAt
        article_source_details.text = article.source?.name
        Glide.with(this).load(article.imageUrl).transform(CenterCrop()).into(banner)
        if(article.saved==true)
        {

            detailsFrag.btnSave.setImageResource(R.drawable.ic_saved)
            var articleSaved = true
            article.saved=true
            saveState = 1

        }
        else
        {
            detailsFrag.btnSave.setImageResource(R.drawable.ic_unsaved)
            article.saved=false
            saveState=0

        }
        var fragment : FragmentManager = supportFragmentManager
        fragment.beginTransaction().show(detailsFrag).hide(headLinesFrag).hide(savedFrag).commit()
    }
}