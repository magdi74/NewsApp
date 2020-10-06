package com.example.newsapp.ui.destinations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newsapp.R
import kotlinx.android.synthetic.main.fragment_item_details.*
import kotlinx.android.synthetic.main.news_card.view.*
import kotlinx.android.synthetic.main.news_card.view.btn_save

class ItemDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_item_details, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        var saveState =0
        btn_save.setOnClickListener({
            if (saveState == 0) {
                btn_save.setImageResource(R.drawable.ic_saved)
                saveState = 1
            }
            else{
                btn_save.setImageResource(R.drawable.ic_unsaved)
                saveState = 0
            }

        })
    }



}