@file:Suppress("UNCHECKED_CAST")

package com.example.newsapp

import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Insets.add
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.example.newsapp.models.Article
import com.example.newsapp.viewmodel.ArticlesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_headlines.*
import kotlinx.android.synthetic.main.fragment_item_details.*
import kotlinx.android.synthetic.main.fragment_saved_items.*

//lateinit var articleMain : ArticleEntity
//lateinit var savedlistTest : ArrayList<ArticleEntity>
var saveState = 0

class MainActivity :  AppCompatActivity() , HeadlinesAdapter.HeadlineListener, SavedItemsAdapter.SavedItemsListener {
    lateinit var llm: LinearLayoutManager

    var currentPageNumber = 1
   // lateinit var newsAdapter: HeadlinesAdapter

    private val mainViewModel: ArticlesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mainViewModel.getNews()
            .observe(this, Observer {
                rv_headlines.adapter = HeadlinesAdapter(it as MutableList<ArticleEntity>?, this)
                attachonScrollListner()
                // saved_rv.adapter = SavedItemsAdapter(List = it as MutableList<Article>?, this)
            })

        mainViewModel.getSavedArticles()
            .observe(this, Observer {
               // rv_headlines.adapter = HeadlinesAdapter(it as MutableList<ArticleEntity>?,this)
              //  mainViewModel.updateSaved()
              //  savedlistTest = it as ArrayList<ArticleEntity>
                saved_rv.adapter = SavedItemsAdapter(it, this)

            })

        mainViewModel.updateSaved()
            .observe(this, Observer {
                //rv_headlines.adapter = HeadlinesAdapter(it as MutableList<ArticleEntity>?,this)

                attachonScrollListner()
                // saved_rv.adapter = SavedItemsAdapter(List = it as MutableList<Article>?, this)
            })

        /*//val navController = findNavController(this, R.id.bottomNavMenu)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavMenu)
        bottomNav?.setupWithNavController(navController)*/
/*
        viewModel.headlinesMutableLiveData.observe(this, object: Observer<MutableList<ArticleEntity>> {
            override fun onChanged(t: MutableList<ArticleEntity>?) {
                if( t!= null ){
                    newsAdapter.appendNews(t)
                    newsAdapter.notifyDataSetChanged()
                    attachonScrollListner()
                }
            }

        })
*/
       // newsAdapter = HeadlinesAdapter(mutableListOf(), this)
        llm = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

      //  rv_headlines.adapter = newsAdapter
        rv_headlines.layoutManager = llm

        //viewModel.getHeadlines(applicationContext)
    //    NewsClient.fetchHeadlines(currentPageNumber, ::onSuccess, ::onError)



        bottomNavMenu!!.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.headlinesFragment -> {
                    mainViewModel.getNews()
                    var fragManager = supportFragmentManager
                    fragManager.beginTransaction().show(headLinesFrag).hide(savedFrag).hide(detailsFrag).commit()
                    /* if(articleMain.saved==true)
                    {
                        headLinesFrag.btnSave.setImageResource(R.drawable.ic_saved)
                    }
                    else
                    {
                        headLinesFrag.btnSave.setImageResource(R.drawable.ic_unsaved)
                    } */
                    true
                }
                R.id.savedItemsFragment -> {
                    mainViewModel.getSavedArticles()
                  //  var SavedAdapter: SavedItemsAdapter = SavedItemsAdapter(savedlistTest,this)
                    var llm_saved: LinearLayoutManager =
                        LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
                 //   saved_rv.adapter = SavedAdapter
                    saved_rv.layoutManager = llm_saved
                    var fragManager = supportFragmentManager
                    fragManager.beginTransaction().show(savedFrag).hide(headLinesFrag).hide(detailsFrag).commit()
                    true
                }
                else -> false
            }

        }
    }
/*
    private fun onError() {
        Toast.makeText(this, "Failed to fetch article", Toast.LENGTH_SHORT).show()
    }

    private fun onSuccess(list: MutableList<ArticleEntity>) {
        newsAdapter.appendNews(list)
        attachonScrollListner()
    }
*/
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
           //         NewsClient.fetchHeadlines(currentPageNumber, ::onSuccess, ::onError)

                   // viewModel.getHeadlines(applicationContext)
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
