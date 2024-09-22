package com.example.zenzoneapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EndQuiz : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_end_quiz)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find the button by its ID
        val signUpButton = findViewById<Button>(R.id.nextButton)

        // Set an onClickListener to handle the button click
        signUpButton.setOnClickListener {
            // Create an intent to navigate to the BaseActivity
            val intent = Intent(this, BaseActivity::class.java)
            startActivity(intent)
        }
    }
}