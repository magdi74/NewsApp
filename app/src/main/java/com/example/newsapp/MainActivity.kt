package com.example.newsapp

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.newsapp.adapters.HeadlinesAdapter
import com.example.newsapp.adapters.SavedItemsAdapter
import com.example.newsapp.database.ArticleEntity
import com.example.newsapp.models.Article
import com.example.newsapp.viewmodel.ArticlesViewModel
import com.example.newsapp.ui.destinations.ItemDetailsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_headlines.*
import kotlinx.android.synthetic.main.fragment_item_details.*
import kotlinx.android.synthetic.main.fragment_saved_items.*
lateinit var articleMain : ArticleEntity
//lateinit var savedlistTest : ArrayList<ArticleEntity>

class MainActivity :  AppCompatActivity() , HeadlinesAdapter.HeadlineListener, SavedItemsAdapter.SavedItemsListener {

    private val articlesViewModel: ArticlesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var B : ActionBar? = supportActionBar
        B?.hide()



        var headlinesAdapter = HeadlinesAdapter(mutableListOf(), this)
        var headlinesLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        var savedAdapter = SavedItemsAdapter(mutableListOf(), this)
        var savedLayoutManager  = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        rv_headlines.adapter = headlinesAdapter
        rv_headlines.layoutManager = headlinesLayoutManager

        articlesViewModel.callNews()

        articlesViewModel.getNews()
            .observe(this, Observer{
                rv_headlines.adapter = HeadlinesAdapter(it as MutableList<ArticleEntity>?, this)
            })


        bottomNavMenu!!.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.headlinesFragment -> {
                    var fragManager = supportFragmentManager
                    fragManager.beginTransaction().show(headLinesFrag).hide(savedFrag).hide(detailsFrag).commit()

                    rv_headlines.adapter = headlinesAdapter
                    rv_headlines.layoutManager = headlinesLayoutManager


                    articlesViewModel.callNews()
                    articlesViewModel.getNews()

                        .observe(this, Observer{
                            rv_headlines.adapter = HeadlinesAdapter(it as MutableList<ArticleEntity>?, this)
                        })

                    true
                }
                R.id.savedItemsFragment -> {
                    var fragManager = supportFragmentManager
                    fragManager.beginTransaction().show(savedFrag).hide(headLinesFrag).hide(detailsFrag).commit()

                    rv_saved.adapter = savedAdapter
                    rv_saved.layoutManager = savedLayoutManager

                    articlesViewModel.callSavedArticles()

                    articlesViewModel.getSavedArticles().observe(this, Observer{
                        rv_saved.adapter = SavedItemsAdapter(it as MutableList<ArticleEntity>?, this)
                    })

                    true
                }
                else -> false
            }

        }
    }


    override fun headlineClicked(article: ArticleEntity) {
        articleMain = article
        detailsFrag.headline_details.text = article.title
        detailsFrag.description.text = article.description
        detailsFrag.article_date_details.text = article.publishedAt
        detailsFrag.article_source_details.text = article.source?.name
        Glide.with(this).load(article.imageUrl).transform(CenterCrop()).into(detailsFrag.banner)
        headlineSaveStatus(article)
        var fragment : FragmentManager
        fragment = supportFragmentManager
        fragment.beginTransaction().show(detailsFrag).hide(headLinesFrag).hide(savedFrag).commit()

    }

    override fun headlineSaveStatus(article: ArticleEntity) {
        articlesViewModel.updateSaved(article)
        articlesViewModel.callNews()
        articlesViewModel.callSavedArticles()
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

    }

    override fun savedItemsSaved(article: ArticleEntity) {
        articlesViewModel.updateSaved(article)
       articlesViewModel.callNews()
        articlesViewModel.callSavedArticles()
    }

    fun copyText(view: View){
        copyToClipboard(articleMain.url.toString())
        Toast.makeText(this, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    fun copyToClipboard(text:CharSequence){
        val clipboard: ClipboardManager =getSystemService(CLIPBOARD_SERVICE)  as ClipboardManager
        val clip:ClipData = ClipData.newPlainText("Link copied to Clipboard",text)
        clipboard?.setPrimaryClip(clip)

    }
}
