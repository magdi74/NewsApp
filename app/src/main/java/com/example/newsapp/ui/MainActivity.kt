package com.example.newsapp.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.newsapp.R
import com.example.newsapp.adapters.HeadlinesAdapter
import com.example.newsapp.adapters.SavedItemsAdapter
import com.example.newsapp.database.ArticleEntity
import com.example.newsapp.models.ArticlesResponse
import com.example.newsapp.tools.Constants
import com.example.newsapp.viewmodel.ArticlesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_headlines.*
import kotlinx.android.synthetic.main.fragment_item_details.*
import kotlinx.android.synthetic.main.fragment_saved_items.*
import kotlinx.android.synthetic.main.fragment_web.*

lateinit var articleMain : ArticleEntity
lateinit var savedAdapter: SavedItemsAdapter
lateinit var headlinesAdapter:  HeadlinesAdapter
lateinit var headlinesLayoutManager: LinearLayoutManager
var pageNumber: Int = 1
var prevFrag: Int = 0
var currentFrag: Int = 0


class MainActivity :  AppCompatActivity() , HeadlinesAdapter.HeadlineListener, SavedItemsAdapter.SavedItemsListener {
    var backPressedTime: Long = 0
    lateinit var backToast: Toast
    private val articlesViewModel: ArticlesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var B : ActionBar? = supportActionBar
        B?.hide()

        Toast.makeText(applicationContext, "Press on Headlines Icon to Refresh", Toast.LENGTH_LONG).show()

        currentFrag = R.id.headLinesFrag
        prevFrag = R.id.headLinesFrag

        headlinesAdapter = HeadlinesAdapter(mutableListOf(), this)
        headlinesLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

         savedAdapter = SavedItemsAdapter(mutableListOf(), this)
        var savedLayoutManager  = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        rv_headlines.adapter = headlinesAdapter
        rv_headlines.layoutManager = headlinesLayoutManager

        articlesViewModel.callNews(pageNumber)

        articlesViewModel.getNews()
            .observe(this, Observer {
                headlinesAdapter = HeadlinesAdapter(it, this)
                rv_headlines.adapter = headlinesAdapter
                headlinesAdapter.notifyDataSetChanged()
                attachScrollListener()

            })
        articlesViewModel.callSavedArticles()

        articlesViewModel.getSavedArticles().observe(this, Observer {
            savedAdapter = SavedItemsAdapter(it, this)
            rv_saved.adapter = savedAdapter
            rv_saved.layoutManager = savedLayoutManager
            savedAdapter.notifyDataSetChanged()
        })


        bottomNavMenu!!.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.headlinesFragment -> {
                    var fragManager = supportFragmentManager
                    fragManager.beginTransaction().show(headLinesFrag).hide(savedFrag).hide(
                        detailsFrag
                    ).commit()

                    rv_headlines.adapter = headlinesAdapter
                    rv_headlines.layoutManager = headlinesLayoutManager

                    prevFrag = currentFrag
                    currentFrag = R.id.headLinesFrag

                    articlesViewModel.callNews(pageNumber)
                    attachScrollListener()
                    articlesViewModel.getNews().observe(this, Observer {
                        headlinesAdapter = HeadlinesAdapter(it , this)
                        rv_headlines.adapter = headlinesAdapter
                        headlinesAdapter.notifyDataSetChanged()
                        attachScrollListener()

                    })

                    true
                }
                R.id.savedItemsFragment -> {
                    var fragManager = supportFragmentManager
                    fragManager.beginTransaction().show(savedFrag).hide(headLinesFrag).hide(
                        detailsFrag
                    ).commit()

                    prevFrag = currentFrag
                    currentFrag = R.id.savedFrag

                    rv_saved.adapter = savedAdapter
                    rv_saved.layoutManager = savedLayoutManager

                    articlesViewModel.callSavedArticles()

                    articlesViewModel.getSavedArticles().observe(this, Observer {
                        savedAdapter = SavedItemsAdapter(it , this)
                        rv_saved.adapter = savedAdapter
                        savedAdapter.notifyDataSetChanged()
                    })

                    true
                }
                else -> false
            }

        }
       // attachOnScrollListener(this, this)
    }
    override fun headlineClicked(article: ArticleEntity) {
        articleMain = article
        detailsFrag.headline_details.text = article.title
        detailsFrag.description.text = article.description
        detailsFrag.article_date_details.text = article.publishedAt
        detailsFrag.article_source_details.text = article.source?.name
        Glide.with(this).load(article.imageUrl).transform(CenterCrop()).into(detailsFrag.banner)
        var fragment : FragmentManager
        fragment = supportFragmentManager
        fragment.beginTransaction().show(detailsFrag).hide(headLinesFrag).hide(savedFrag).commit()

        prevFrag = currentFrag
        currentFrag = detailsFrag

    }

    override fun headlineSaveStatus(article: ArticleEntity) {
        articlesViewModel.updateSaved(article)
        articlesViewModel.callNews(pageNumber)
        articlesViewModel.callSavedArticles()
        headlinesAdapter.notifyDataSetChanged()
       // attachOnScrollListener(this,MainActivity())
        attachScrollListener()
       /* articlesViewModel.getNews()
            .observe(this, Observer{
                headlinesAdapter = HeadlinesAdapter(it as MutableList<ArticleEntity>?, this)
                rv_headlines.adapter = headlinesAdapter
                headlinesAdapter.notifyDataSetChanged()
            })*/
    }

    override fun savedItemsClicked(article: ArticleEntity) {
        articleMain = article
        detailsFrag.headline_details.text = article.title
        detailsFrag.description.text = article.description
        detailsFrag.article_date_details.text = article.publishedAt
        detailsFrag.article_source_details.text = article.source?.name
        Glide.with(this).load(article.imageUrl).transform(CenterCrop()).into(detailsFrag.banner)
        var fragment : FragmentManager
        fragment = supportFragmentManager
        fragment.beginTransaction().show(detailsFrag).hide(headLinesFrag).hide(savedFrag).commit()

        prevFrag = currentFrag
        currentFrag = R.id.detailsFrag

    }

    override fun savedItemsSaved(article: ArticleEntity) {
        articlesViewModel.updateSaved(article)
        articlesViewModel.callNews(pageNumber)
        articlesViewModel.callSavedArticles()
        savedAdapter.notifyDataSetChanged()
        articlesViewModel.getSavedArticles().observe(this, Observer {
            savedAdapter = SavedItemsAdapter(it, this)
            rv_saved.adapter = savedAdapter
            savedAdapter.notifyDataSetChanged()
        })
        articlesViewModel.getNews()
            .observe(this, Observer {
                headlinesAdapter = HeadlinesAdapter(it as MutableList<ArticleEntity>?, this)
                rv_headlines.adapter = headlinesAdapter
                headlinesAdapter.notifyDataSetChanged()
            })
        headlinesAdapter.notifyDataSetChanged()
    }

    fun copyText(view: View){
        copyToClipboard(articleMain.url.toString())
        Toast.makeText(this, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    fun copyToClipboard(text: CharSequence){
        val clipboard: ClipboardManager =getSystemService(CLIPBOARD_SERVICE)  as ClipboardManager
        val clip:ClipData = ClipData.newPlainText("Link copied to Clipboard", text)
        clipboard?.setPrimaryClip(clip)

    }

    override fun onBackPressed() {
        if (web_frag.web_view.canGoBack())web_frag.web_view.goBack()
        else if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            /*backToast = Toast.makeText(
                getBaseContext(),
                "Press back again to exit",
                Toast.LENGTH_SHORT
            );
            backToast.show();*/

            when(currentFrag){
                R.id.headLinesFrag -> finish()
                R.id.savedFrag -> {
                    if(prevFrag == R.id.headLinesFrag){
                        supportFragmentManager.beginTransaction()
                            .show(headLinesFrag).hide(savedFrag).hide(detailsFrag).commit()
                    }
                    else if(prevFrag == R.id.detailsFrag)
                        supportFragmentManager.beginTransaction()
                            .show(detailsFrag).hide(savedFrag).hide(headLinesFrag).commit()
                }
                R.id.detailsFrag -> {
                    if(prevFrag == R.id.savedFrag){
                        supportFragmentManager.beginTransaction()
                            .show(savedFrag).hide(detailsFrag).hide(headLinesFrag).commit()
                    }
                    else if(prevFrag == R.id.headLinesFrag){
                        supportFragmentManager.beginTransaction()
                            .show(headLinesFrag).hide(savedFrag).hide(detailsFrag).commit()

                    }

                }
            }
        }
        backPressedTime = System.currentTimeMillis();
    }
/*
    fun attachOnScrollListener(owner: LifecycleOwner, context: HeadlinesAdapter.HeadlineListener){
        rv_headlines.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItems = headlinesLayoutManager.itemCount
                val visibleItemsCount = headlinesLayoutManager.childCount
                val firstVisibleItem = headlinesLayoutManager.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemsCount >= totalItems / 2) {
                    rv_headlines.removeOnScrollListener(this)
                    if (pageNumber <= Constants.MAX_PAGES) {
                        pageNumber++
                        articlesViewModel.callNews(pageNumber)
                        articlesViewModel.getNews()
                            .observe(owner, Observer {
                                headlinesAdapter = HeadlinesAdapter(
                                    it as MutableList<ArticleEntity>?,
                                    context
                                )
                                rv_headlines.adapter = headlinesAdapter
                                headlinesAdapter.notifyDataSetChanged()
                            })
                        headlinesAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }*/
    fun attachScrollListener(){
        rv_headlines.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItems = headlinesLayoutManager.itemCount
                val visibleItemsCount = headlinesLayoutManager.childCount
                val firstVisibleItem = headlinesLayoutManager.findLastVisibleItemPosition()

                if (firstVisibleItem + visibleItemsCount >= totalItems / 2) {
                    rv_headlines.removeOnScrollListener(this)
                    pageNumber++
                    articlesViewModel.callNews(pageNumber)
                }
            }
        })
    }
}
