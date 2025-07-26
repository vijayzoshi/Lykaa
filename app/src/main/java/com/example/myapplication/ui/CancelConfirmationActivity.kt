package com.example.myapplication.ui

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.myapplication.R

class CancelConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cancel_confirmation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val lottieView = findViewById<LottieAnimationView>(R.id.lottieView)

     //   val intent = Intent(this, SessionActivity :: class.java) // Replace MainActivity with your target


        lottieView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                println("Animation started!")
            }

            override fun onAnimationEnd(animation: Animator) {
             //   startActivity(intent)
                finish()
            }

            override fun onAnimationCancel(animation: Animator) {
                println("Animation canceled!")
            }

            override fun onAnimationRepeat(animation: Animator) {
                println("Animation repeated!")
            }
        })
    }
}