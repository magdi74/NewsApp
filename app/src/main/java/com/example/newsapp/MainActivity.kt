package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.ui.HeadlinesFragment
import com.example.newsapp.ui.destinations.SavedItemsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(this, R.id.)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavMenu)

        bottomNav?.setupWithNavController(navController)

    }

    private val navListener = object:BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(@NonNull item: MenuItem):Boolean {
            var selectedFragment: Fragment? = null
            when (item.getItemId()) {
                R.id.headlinesFragment -> selectedFragment = HeadlinesFragment()
                R.id.savedItemsFragment -> selectedFragment = SavedItemsFragment()
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFragment!!
            ).commit()
            return true
        }
    }
}