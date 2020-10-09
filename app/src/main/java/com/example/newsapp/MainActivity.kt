package com.example.newsapp

import android.content.ClipData
import android.content.ClipboardManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.adapters.HeadlinesAdapter
import com.example.newsapp.adapters.SavedItemsAdapter
import com.example.newsapp.database.ArticleEntity
import com.example.newsapp.models.Article
import com.example.newsapp.viewmodel.ArticlesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_headlines.*
import kotlinx.android.synthetic.main.fragment_saved_items.*

class MainActivity :  AppCompatActivity() , HeadlinesAdapter.HeadlineListener, SavedItemsAdapter.SavedItemsListener {

    private val articlesViewModel: ArticlesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

    }

    override fun headlineSaveStatus(article: ArticleEntity) {
        articlesViewModel.updateSaved(article)
        articlesViewModel.callNews()
        articlesViewModel.callSavedArticles()
    }

    override fun savedItemsClicked(article: ArticleEntity) {

    }

    override fun savedItemsSaved(article: ArticleEntity) {
        articlesViewModel.updateSaved(article)
        articlesViewModel.callNews()
        articlesViewModel.callSavedArticles()
    }

    fun copyText(view: View){
        //copyToClipboard(articleMain.url.toString())
        Toast.makeText(this, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    fun copyToClipboard(text:CharSequence){
        val clipboard: ClipboardManager =getSystemService(CLIPBOARD_SERVICE)  as ClipboardManager
        val clip:ClipData = ClipData.newPlainText("Link copied to Clipboard",text)
        clipboard?.setPrimaryClip(clip)

    }
}
