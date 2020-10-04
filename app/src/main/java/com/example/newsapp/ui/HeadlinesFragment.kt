package com.example.newsapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newsapp.R
import android.widget.Toast
import com.example.newsapp.Adapters.HeadlinesAdapter


class HeadlinesFragment : Fragment(), HeadlinesAdapter.HeadlineListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_headlines, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun headlineClicked(id: Int) {

        Toast.makeText(context, id , Toast.LENGTH_SHORT).show()

    }


}