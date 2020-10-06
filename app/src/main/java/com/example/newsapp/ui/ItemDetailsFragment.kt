package com.example.newsapp.ui.destinations

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.getSystemServiceName
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.articleMain
import com.example.newsapp.models.Article
import com.example.newsapp.saveState
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_headlines.*
import kotlinx.android.synthetic.main.fragment_item_details.*
import kotlinx.android.synthetic.main.fragment_item_details.view.*

class ItemDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view: View = inflater.inflate(R.layout.fragment_item_details, container, false)



        view.btnSave.setOnClickListener{ item ->
            if (saveState == 0) {
                btnSave.setImageResource(R.drawable.ic_saved)
                articleMain.saved=true
                saveState = 1
            }
            else{
                btnSave.setImageResource(R.drawable.ic_unsaved)
                articleMain.saved = false
                saveState = 0
            }
        }
        return view
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }








}