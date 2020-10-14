package com.example.newsapp.ui.destinations

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.*
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.getSystemServiceName
import com.example.newsapp.*
import com.example.newsapp.models.Article
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_headlines.*
import kotlinx.android.synthetic.main.fragment_item_details.*
import kotlinx.android.synthetic.main.fragment_item_details.view.*
import kotlinx.android.synthetic.main.fragment_webview.*

class ItemDetailsFragment : Fragment() {

    var found: Boolean = false
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view: View = inflater.inflate(R.layout.fragment_item_details, container, false)

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

        view.btnSave.setOnClickListener { item ->
            //var iterator = savedlistTest.iterator()
            if(savedlistTest.isEmpty())
            {
                Toast.makeText(activity,"Article Saved",Toast.LENGTH_SHORT).show()
                savedlistTest.add(articleMain)
            }
            else
            {
                found= false
                for(i in 0 until savedlistTest.size)
                {
                    if(savedlistTest[i].url == articleMain.url)
                    {
                        found= true
                        break;
                    }
                }
                if(!found)
                {
                    Toast.makeText(activity,"Article Saved",Toast.LENGTH_SHORT).show()
                    savedlistTest.add(articleMain)
                }
                else
                {
                    Toast.makeText(activity,"Article Already Saved",Toast.LENGTH_SHORT).show()
                }
            }

        }
        return view
    }
}