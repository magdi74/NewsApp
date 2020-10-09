package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.newsapp.R
import com.example.newsapp.database.ArticleEntity
import com.example.newsapp.database.ArticlesDatabase
import com.example.newsapp.models.Article
import com.example.newsapp.saveState
//import com.example.newsapp.savedlistTest
import kotlinx.android.synthetic.main.news_card.view.*

lateinit var list : ArrayList<ArticleEntity>
lateinit var database: ArticlesDatabase
class HeadlinesAdapter(private val List: MutableList<ArticleEntity>?, var Listener: HeadlineListener):
    RecyclerView.Adapter<HeadlinesAdapter.NewsViewHolder>() {
    public interface HeadlineListener {
        fun headlineClicked(article: ArticleEntity,position: Int)
    }
    inner class NewsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun onBind(article: ArticleEntity,position: Int){
            itemView.article_headline.text= article.title
            itemView.article_source.text = article.source!!.name
            itemView.article_date.text = article.publishedAt
            Glide.with(itemView).load(article.imageUrl).transform(CenterCrop()).into(itemView.article_poster)

            itemView.setOnClickListener({Listener.headlineClicked(article,position)})

            itemView.btnSave.setOnClickListener{
                if (article.saved == false) {

                    itemView.btnSave.setImageResource(R.drawable.ic_saved)
                    article.saved = true
                  //  database.ArticlesDao().updateArticleSaveStatus(article as MutableList<ArticleEntity>)
                   // list.add(article)
                    //savedlistTest.add(article)
                }
                else{
                    itemView.btnSave.setImageResource(R.drawable.ic_unsaved)
                    article.saved = false
                  //  database.ArticlesDao().updateArticleSaveStatus(article as MutableList<ArticleEntity>)

                   // savedlistTest.remove(article)
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder
            = NewsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.news_card,parent,false))
    override fun getItemCount(): Int= List!!.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.onBind(List!![position],position)
    }

    fun appendNews(articles : MutableList<ArticleEntity>)
    {
        this.List!!.addAll(articles)
        notifyItemRangeInserted(this.List!!.size,this.List.size-1)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.article_poster
        val textView1: TextView = itemView.article_headline
        val textView2: TextView = itemView.article_source
        val textView3: TextView = itemView.article_date

    }




}