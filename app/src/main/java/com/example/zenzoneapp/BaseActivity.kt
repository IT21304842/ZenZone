package com.example.zenzoneapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base) // Layout with BottomNavigationView

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigation)

        // Set the default fragment (Home) when the activity is created
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame, Home())
                .commit()
        }

        // Set up the navigation listener
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    replaceFragment(Home())
                    true
                }
                R.id.bottom_schedule -> {
                    replaceFragment(ScheduleFragment())
                    true
                }
//                R.id.bottom_therapy -> {
//                    replaceFragment(TherapyFragment())
//                    true
//                }
//                R.id.bottom_connect -> {
//                    replaceFragment(ConnectFragment())
//                    true
//                }
//                R.id.bottom_profile -> {
//                    replaceFragment(ProfileFragment())
//                    true
//                }
                else -> false
            }
        }
    }

    // Method to replace the current fragment with a new one
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame, fragment)
            .commit()
    }
}
