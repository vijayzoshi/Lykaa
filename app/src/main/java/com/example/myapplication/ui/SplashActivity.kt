package com.example.myapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth

    private val SPLASH_DISPLAY_LENGTH = 2000L // 2 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()



        Handler(Looper.getMainLooper()).postDelayed({

            // Check if user is already signed in
            if (auth.currentUser != null) {
                // User is already signed in, send to main activity
                startActivity(Intent(this, MainActivity2::class.java))
                finish()

            }else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()

            }

        }, SPLASH_DISPLAY_LENGTH)

    }
}