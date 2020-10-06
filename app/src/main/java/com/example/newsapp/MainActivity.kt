package com.example.newsapp

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.get
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.newsapp.adapters.HeadlinesAdapter
import com.example.newsapp.apiclient.NewsClient
import com.example.newsapp.models.Article
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_headlines.*
import kotlinx.android.synthetic.main.fragment_item_details.*
import kotlinx.android.synthetic.main.fragment_item_details.view.*
import java.text.FieldPosition

var saveState = 0
    lateinit var articleMain : Article

class MainActivity :  AppCompatActivity() , HeadlinesAdapter.HeadlineListener {
    lateinit var llm: LinearLayoutManager
    var currentPageNumber = 1
    lateinit var newsAdapter: HeadlinesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*//val navController = findNavController(this, R.id.bottomNavMenu)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavMenu)

        bottomNav?.setupWithNavController(navController)*/

        newsAdapter = HeadlinesAdapter(mutableListOf(), this)
        llm = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        rv_headlines.adapter = newsAdapter
        rv_headlines.layoutManager = llm

        NewsClient.fetchHeadlines(currentPageNumber, ::onSuccess, ::onError)



        bottomNavMenu!!.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.headlinesFragment -> {
                    var fragManager = supportFragmentManager
                    fragManager.beginTransaction().show(headLinesFrag).hide(savedFrag).hide(detailsFrag).commit()
                    if(articleMain.saved==true)
                    {
                        headLinesFrag.btnSave.setImageResource(R.drawable.ic_saved)
                    }
                    else
                    {
                        headLinesFrag.btnSave.setImageResource(R.drawable.ic_unsaved)
                    }
                    true
                }
                R.id.savedItemsFragment -> {
                    var fragManager = supportFragmentManager
                    fragManager.beginTransaction().show(savedFrag).hide(headLinesFrag).hide(detailsFrag).commit()
                    true
                }
                else -> false
            }

        }
    }

    private fun onError() {
        Toast.makeText(this, "Failed to fetch article", Toast.LENGTH_SHORT).show()
    }

    private fun onSuccess(list: MutableList<Article>) {
        newsAdapter.appendNews(list)
        attachonScrollListner()
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
                    NewsClient.fetchHeadlines(currentPageNumber, ::onSuccess, ::onError)
                }
            }
        })

    }

    override fun headlineClicked(article: Article,position: Int) {
        articleMain= article
        headline_details.text = article.title
        description.text = article.description
        article_date_details.text = article.publishedAt
        article_source_details.text = article.source.name
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
        copyToClipboard(articleMain.url.toString())
        Toast.makeText(this, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    fun copyToClipboard(text:CharSequence){
        val clipboard: ClipboardManager =getSystemService(CLIPBOARD_SERVICE)  as ClipboardManager
        val clip:ClipData = ClipData.newPlainText("Link copied to Clipboard",text)
        clipboard?.setPrimaryClip(clip)

    }
}







    /*private val navListener = object:BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(@NonNull item: MenuItem):Boolean {
            var selectedFragment: Fragment? = null
            when (item.getItemId()) {
                R.id.headlinesFragment -> selectedFragment = HeadlinesFragment()
                R.id.savedItemsFragment -> selectedFragment = SavedItemsFragment()
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFragment!!
            ).commit()
            return true
        }
    }*/
