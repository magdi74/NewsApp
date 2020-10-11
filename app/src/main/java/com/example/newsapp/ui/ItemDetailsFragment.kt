package com.example.newsapp.ui.destinations

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.newsapp.*
import com.example.newsapp.adapters.HeadlinesAdapter
import kotlinx.android.synthetic.main.fragment_item_details.view.*
import com.example.newsapp.articleMain
import com.example.newsapp.database.ArticleEntity
import com.example.newsapp.viewmodel.ArticlesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_web.*

class ItemDetailsFragment : Fragment()  {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view: View = inflater.inflate(R.layout.fragment_item_details, container, false)

        val articlesViewModel: ArticlesViewModel by viewModels()
        view.btnSave.setOnClickListener{
            Toast.makeText(activity?.applicationContext,"Article Saved",Toast.LENGTH_SHORT).show()
            articleMain.saved = true
            articlesViewModel.updateSaved(articleMain)
        }

        view.read_full_story_btn.setOnClickListener{
            web_frag. web_view.webViewClient = WebViewClient()
            web_frag. web_view.apply {
                loadUrl(articleMain.url)
                settings.javaScriptEnabled = true
                settings.safeBrowsingEnabled = true
            }

            var fragmentManager = activity?.supportFragmentManager
            fragmentManager?.beginTransaction()?.show(web_frag)?.hide(detailsFrag)?.hide(headLinesFrag)
                ?.hide(savedFrag)?.commit()
        }
        var  mutableLiveData : MutableLiveData<ArticleEntity> = MutableLiveData()
      /*  view.btnSave.setOnClickListener{ item ->


            if(articleMain.saved == false){
                Toast.makeText(activity,"Article Saved", Toast.LENGTH_SHORT).show()
                articleMain.saved = true
                 //savedlistTest.add(articleMain)

            }
        }*/
        return view
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }

}