package com.example.newsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.newsapp.R
import com.example.newsapp.database.ArticleEntity
import kotlinx.android.synthetic.main.news_card.view.*

private val context: Context? = null
class HeadlinesAdapter(
    private val List: MutableList<ArticleEntity>?,
    var Listener: HeadlineListener
):
    RecyclerView.Adapter<HeadlinesAdapter.NewsViewHolder>() {

    interface HeadlineListener {
        fun headlineClicked(article: ArticleEntity)
        fun headlineSaveStatus(article: ArticleEntity)
    }

    inner class NewsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun onBind(article: ArticleEntity){
            itemView.article_headline.text= article.title
            itemView.article_source.text = article.source!!.name
            itemView.article_date.text = article.publishedAt
            Glide.with(itemView).load(article.imageUrl).transform(CenterCrop()).into(itemView.article_poster)





           /* if (article.saved == true){
               // Toast.makeText(context, "Article saved", Toast.LENGTH_SHORT).show()

              //  itemView.btnSave.setImageResource(R.drawable.ic_saved)

            }else if(article.saved == false){

             //   Toast.makeText(context, "Article Unsaved", Toast.LENGTH_SHORT).show()
                //itemView.btnSave.setImageResource(R.drawable.ic_unsaved)
            }

            */
            itemView.setOnClickListener {
                Listener.headlineClicked(article)
                Listener.headlineSaveStatus(article)
            }
            val context = itemView.context

            itemView.btnSave.setOnClickListener{

        if(article.saved == false){//save scenario

                    article.saved = true

                    Toast.makeText(context, "Article saved", Toast.LENGTH_SHORT).show()

                    //itemView.btnSave.setImageResource(R.drawable.ic_saved)
                }
                Listener.headlineSaveStatus(article)
                article.saved= false
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder
            = NewsViewHolder(

        LayoutInflater.from(parent.context).inflate(
            R.layout.news_card,
            parent,
            false
        )
    )

    override fun getItemCount(): Int= List!!.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.onBind(List!![position])
    }
}