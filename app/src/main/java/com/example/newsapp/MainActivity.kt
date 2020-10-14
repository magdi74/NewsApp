package com.example.newsapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.webkit.WebViewClient
import android.widget.Adapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.core.view.get
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.newsapp.adapters.HeadlinesAdapter
import com.example.newsapp.adapters.SavedItemsAdapter
import com.example.newsapp.apiclient.NewsClient
import com.example.newsapp.models.Article
import com.example.newsapp.ui.destinations.SavedItemsFragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_headlines.*
import kotlinx.android.synthetic.main.fragment_item_details.*
import kotlinx.android.synthetic.main.fragment_item_details.view.*
import kotlinx.android.synthetic.main.fragment_saved_items.*
import kotlinx.android.synthetic.main.fragment_webview.*
import java.text.FieldPosition

lateinit var articleMain : Article
var savedlistTest : ArrayList<Article> = ArrayList()

class MainActivity :  AppCompatActivity() , SavedItemsAdapter.SavedItemsListener{
    lateinit var llm: LinearLayoutManager
    var currentPageNumber = 1
    lateinit var newsAdapter: HeadlinesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var B : ActionBar? = supportActionBar
        B?.hide()
        savedlistTest= loadData()
        bottomNavMenu!!.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.headlinesFragment -> {
                    var fragManager = supportFragmentManager
                    fragManager.beginTransaction().show(headLinesFrag).hide(savedFrag)
                        .hide(detailsFrag).commit()
                    true
                }
                R.id.savedItemsFragment -> {
                    lateinit var llm_saved: LinearLayoutManager
                    lateinit var SavedAdapter: SavedItemsAdapter
                    SavedAdapter = SavedItemsAdapter(savedlistTest, this)
                    llm_saved = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    saved_rv.adapter = SavedAdapter
                    saved_rv.layoutManager = llm_saved
                    var fragManager = supportFragmentManager
                    fragManager.beginTransaction().show(savedFrag).hide(headLinesFrag)
                        .hide(detailsFrag).commit()
                    saveData(savedlistTest)
                    true
                }
                else -> false
            }

        }
    }
    fun copyText(view: View) {
        copyToClipboard(articleMain.url.toString())
        Toast.makeText(this, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    fun copyToClipboard(text: CharSequence) {
        val clipboard: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("Link copied to Clipboard", text)
        clipboard?.setPrimaryClip(clip)

    }
    override fun onBackPressed() {
        if (web_frag.web_view.canGoBack())web_frag.web_view.goBack()
        else {
            saveData(savedlistTest)
            super.onBackPressed()
        }

    }

    override fun savedItemsClicked(article: Article) {
        articleMain = article
        detailsFrag.headline_details.text = article.title
        detailsFrag.description.text = article.description
        detailsFrag.article_date_details.text = article.publishedAt
        detailsFrag.article_source_details.text = article.source.name
        Glide.with(this).load(article.imageUrl).transform(CenterCrop()).into(detailsFrag.banner)
        var fragment =supportFragmentManager
        fragment.beginTransaction().show(detailsFrag).hide(headLinesFrag).hide(savedFrag).commit()
    }

    fun saveData(list : ArrayList<Article>)
    {
        lateinit var text : String
        val sharedPreferences : SharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        var counter = 0
        editor.apply{
            putInt("ListSize",list.size)
            for(articles in list)
            {
                text =Gson().toJson(articles)
                putString("article"+counter,text)
                counter++
            }
        }.apply()
    }

    fun loadData() : ArrayList<Article>
    {
        var list = ArrayList<Article>()
        lateinit var article : Article
        var size : Int
        lateinit var text : String
        val sharedPreferences : SharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        size = sharedPreferences.getInt("ListSize",0)
        for(i in 0 until size)
        {
            text= sharedPreferences.getString("article"+i,null).toString()
            article = Gson().fromJson(text,Article::class.java)
            list.add(article)
        }
        return list
    }
}








