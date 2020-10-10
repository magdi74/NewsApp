package com.example.newsapp.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.newsapp.*
import com.example.newsapp.adapters.HeadlinesAdapter
import com.example.newsapp.apiclient.NewsClient
import com.example.newsapp.models.Article
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_headlines.*
import kotlinx.android.synthetic.main.fragment_item_details.*


class HeadlinesFragment : Fragment()  , HeadlinesAdapter.HeadlineListener {

    var currentPageNumber = 1
    lateinit var newsAdapter: HeadlinesAdapter
    lateinit var llm: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_headlines, container, false)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsAdapter = HeadlinesAdapter(mutableListOf(), this)
        llm = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        rv_headlines.adapter = newsAdapter
        rv_headlines.layoutManager = llm

        NewsClient.fetchHeadlines(currentPageNumber, ::onSuccess, ::onError)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun onError() {
        Toast.makeText(activity, "Failed to fetch article", Toast.LENGTH_SHORT).show()
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

    override fun headlineClicked(article: Article, position: Int) {
        articleMain = article
        detailsFrag.headline_details.text = article.title
        detailsFrag.description.text = article.description
        detailsFrag.article_date_details.text = article.publishedAt
        detailsFrag.article_source_details.text = article.source.name
        Glide.with(this).load(article.imageUrl).transform(CenterCrop()).into(detailsFrag.banner)
        var fragment = activity?.supportFragmentManager
        fragment?.beginTransaction()?.show(detailsFrag)?.hide(headLinesFrag)?.hide(savedFrag)?.commit()

    }



}