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
import kotlinx.android.synthetic.main.news_card.view.*


class SavedItemsAdapter(private val List: MutableList<ArticleEntity>?, var Listener: SavedItemsListener):
    RecyclerView.Adapter<SavedItemsAdapter.NewsViewHolder>() {
    public interface SavedItemsListener {
        fun savedItemsClicked(article: ArticleEntity)
    }
    inner class NewsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        fun onBind(article: ArticleEntity){
            itemView.article_headline.text= article.title
            itemView.article_source.text = article.source.toString()
            itemView.article_date.text = article.publishedAt
            Glide.with(itemView).load(article.imageUrl).transform(CenterCrop()).into(itemView.article_poster)
            var saveState = 0

            itemView.setOnClickListener({Listener.savedItemsClicked(article)})

     /**       itemView.btnSave.setOnClickListener({
                if (saveState == 0) {
                    itemView.btnSave.setImageResource(R.drawable.ic_saved)
                    saveState = 1
                }
                else{
                    itemView.btnSave.setImageResource(R.drawable.ic_unsaved)
                    saveState = 0
                }
            }) **/
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder
            = NewsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.saved_articles_card,parent,false))
    override fun getItemCount(): Int= List!!.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.onBind(List!![position])
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