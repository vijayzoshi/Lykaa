package com.example.myapplication.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.example.myapplication.R
import com.google.android.material.appbar.MaterialToolbar

class HelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)


         val toolbar : MaterialToolbar
        toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }


        val callCd = findViewById<CardView>(R.id.cd_call)
        val chatCd = findViewById<CardView>(R.id.cd_wp)

        callCd.setOnClickListener {


            dialPhoneNumber("+918377055197")

        }

        chatCd.setOnClickListener {

            openWhatsapp( "https://wa.me/918377055197")

        }


    }

    fun dialPhoneNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }


    }

    fun openWhatsapp(url: String) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

}