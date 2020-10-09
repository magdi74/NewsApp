package com.example.newsapp

import android.content.ClipData
import android.content.ClipboardManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.newsapp.adapters.HeadlinesAdapter
import com.example.newsapp.adapters.SavedItemsAdapter
import com.example.newsapp.models.Article
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity :  AppCompatActivity() , HeadlinesAdapter.HeadlineListener, SavedItemsAdapter.SavedItemsListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavMenu!!.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.headlinesFragment -> {
                    var fragManager = supportFragmentManager
                    fragManager.beginTransaction().show(headLinesFrag).hide(savedFrag).hide(detailsFrag).commit()
                    true
                }
                R.id.savedItemsFragment -> {
                    var fragManager = supportFragmentManager
                    fragManager.beginTransaction().show(savedFrag).hide(headLinesFrag).hide(detailsFrag).commit()
                    true
                }
                else -> false
            }

        }
    }


    override fun headlineClicked(article: Article) {

    }

    override fun savedItemsClicked(article: Article) {

    }

    fun copyText(view: View){
        //copyToClipboard(articleMain.url.toString())
        Toast.makeText(this, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    fun copyToClipboard(text:CharSequence){
        val clipboard: ClipboardManager =getSystemService(CLIPBOARD_SERVICE)  as ClipboardManager
        val clip:ClipData = ClipData.newPlainText("Link copied to Clipboard",text)
        clipboard?.setPrimaryClip(clip)

    }
}
