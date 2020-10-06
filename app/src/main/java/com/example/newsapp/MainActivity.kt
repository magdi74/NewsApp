package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.replace
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.adapters.HeadlinesAdapter
import com.example.newsapp.apiclient.NewsClient
import com.example.newsapp.models.Article
import com.example.newsapp.ui.HeadlinesFragment
import com.example.newsapp.ui.destinations.SavedItemsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_headlines.*


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
                    fragManager.beginTransaction().show(headLinesFrag).hide(savedFrag).commit()
                    true
                }
                R.id.savedItemsFragment -> {
                    var fragManager = supportFragmentManager
                    fragManager.beginTransaction().show(savedFrag).hide(headLinesFrag).commit()
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

    override fun headlineClicked(article: Article) {
        Toast.makeText(this,"ay 7aga",Toast.LENGTH_SHORT).show()
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
