package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.newsapp.Item
import com.example.newsapp.R
import com.example.newsapp.models.Article
import kotlinx.android.synthetic.main.news_card.view.*


class HeadlinesAdapter(private val List: MutableList<Article>?, var Listener: HeadlineListener):
    RecyclerView.Adapter<HeadlinesAdapter.NewsViewHolder>() {
    public interface HeadlineListener {
        fun headlineClicked(article: Article)
    }
    inner class NewsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun onBind(article: Article){
            itemView.article_headline.text= article.title
            itemView.article_source.text = article.source.toString()
            itemView.article_date.text = article.publishedAt
            Glide.with(itemView).load(article.imageUrl).transform(CenterCrop()).into(itemView.article_poster)
            var saveState = 0

            itemView.setOnClickListener({Listener.headlineClicked(article)})

            itemView.btn_save.setOnClickListener({
                if (saveState == 0) {
                    itemView.btn_save.setImageResource(R.drawable.ic_saved)
                    saveState = 1
                }
                else{
                    itemView.btn_save.setImageResource(R.drawable.ic_unsaved)
                    saveState = 0
                }
            })
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder
            = NewsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.news_card,parent,false))
    override fun getItemCount(): Int= List!!.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.onBind(List!![position])
    }

    fun appendNews(articles : List<Article>)
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