package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.newsapp.R
import com.example.newsapp.database.ArticleEntity
import com.example.newsapp.models.Article
import kotlinx.android.synthetic.main.news_card.view.*


class SavedItemsAdapter(private val List: MutableList<ArticleEntity>?, var Listener: SavedItemsListener):
    RecyclerView.Adapter<SavedItemsAdapter.NewsViewHolder>() {

     interface SavedItemsListener {
         fun savedItemsClicked(article: ArticleEntity)
         fun savedItemsSaved(article:ArticleEntity)
    }

    inner class NewsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun onBind(article: ArticleEntity){
            itemView.article_headline.text= article.title
            itemView.article_source.text = article.source!!.name
            itemView.article_date.text = article.publishedAt
            Glide.with(itemView).load(article.imageUrl).transform(CenterCrop()).into(itemView.article_poster)

            if(article.saved == true)
                itemView.btnSave.setImageResource(R.drawable.ic_saved)
            else if(article.saved == false)
                itemView.btnSave.setImageResource(R.drawable.ic_unsaved)

            itemView.setOnClickListener { Listener.savedItemsClicked(article) }

            itemView.btnSave.setOnClickListener{
                if(article.saved == true){//unsave scenario
                    article.saved = false
                    itemView.btnSave.setImageResource(R.drawable.ic_unsaved)
                }
                else if(article.saved == false){//save scenario
                    article.saved = true
                    itemView.btnSave.setImageResource(R.drawable.ic_saved)
                }
                Listener.savedItemsSaved(article)
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