package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.newsapp.Item
import com.example.newsapp.R
import kotlinx.android.synthetic.main.news_card.view.*


class HeadlinesAdapter(private val List: MutableList<Item>, val Listener: HeadlineListener): RecyclerView.Adapter<HeadlinesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.news_card, parent, false)

        return ViewHolder(itemView)
    }

    override fun getItemCount() = List.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem =List[position]

        holder.imageView.setImageResource(currentItem.imageResource)
        holder.textView1.text=currentItem.newsHeadline
        holder.textView2.text=currentItem.newsDate
        holder.textView3.text=currentItem.newsSource

        holder.itemView.setOnClickListener({Listener.headlineClicked(1)})

    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.article_poster
        val textView1: TextView = itemView.article_headline
        val textView2: TextView = itemView.article_source
        val textView3: TextView = itemView.article_date

    }

    public interface HeadlineListener {

        fun headlineClicked(id: Int)


    }
}