package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.newsapp.R
import com.example.newsapp.models.Article
import com.example.newsapp.savedlistTest
import kotlinx.android.synthetic.main.news_card.view.article_date
import kotlinx.android.synthetic.main.news_card.view.article_headline
import kotlinx.android.synthetic.main.news_card.view.article_poster
import kotlinx.android.synthetic.main.news_card.view.article_source
import kotlinx.android.synthetic.main.saved_articles_card.view.*


class SavedItemsAdapter(private val List: MutableList<Article>?, var Listener: SavedItemsListener):
    RecyclerView.Adapter<SavedItemsAdapter.NewsViewHolder>() {
    public interface SavedItemsListener {
        fun savedItemsClicked(article: Article)
    }
    inner class NewsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun onBind(article: Article){
            itemView.article_headline.text= article.title
            itemView.article_source.text = article.source.name.toString()
            itemView.article_date.text = article.publishedAt
            Glide.with(itemView).load(article.imageUrl).transform(CenterCrop()).into(itemView.article_poster)
            var saveState = 0

            itemView.setOnClickListener({Listener.savedItemsClicked(article)})

            itemView.btnSave.setOnClickListener{
                savedlistTest.remove(article)
                notifyDataSetChanged()
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder
            = NewsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.saved_articles_card,parent,false))
    override fun getItemCount(): Int= List!!.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.onBind(List!![position])
    }
}