package com.example.newsapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import kotlinx.android.synthetic.main.activity_splash_.*
import java.util.*

class Splash_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Objects.requireNonNull(getSupportActionBar()!!).hide()
        setContentView(R.layout.activity_splash_)

        splash_img.alpha = 0f
        splash_img.animate().setDuration(3000).alpha(1f).withEndAction{
            val i = Intent(this,MainActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()
        }
    }
}