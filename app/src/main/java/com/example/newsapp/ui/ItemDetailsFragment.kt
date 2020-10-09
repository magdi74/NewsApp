package com.example.newsapp.ui.destinations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newsapp.*
import kotlinx.android.synthetic.main.fragment_item_details.view.*

class ItemDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view: View = inflater.inflate(R.layout.fragment_item_details, container, false)



        view.btnSave.setOnClickListener{ item ->

        }
        return view
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }
}