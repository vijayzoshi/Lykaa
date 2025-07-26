package com.example.myapplication.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.myapplication.PhonenoActivity
import com.example.myapplication.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class NameActivity : AppCompatActivity() {



    private lateinit var databaseReference: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name)



        val usernameTf = findViewById<TextInputLayout>(R.id.tf_username)
        val nextBtn = findViewById<Button>(R.id.btn_next)

        nextBtn.setOnClickListener {

            val username = usernameTf.editText?.text.toString()

            val intent = Intent(this, PhonenoActivity::class.java)
            intent.putExtra("username", username)

            startActivity(intent)
        }




    }
}