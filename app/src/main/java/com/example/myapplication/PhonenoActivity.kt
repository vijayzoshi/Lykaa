package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.ui.MainActivity2
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PhonenoActivity : AppCompatActivity() {




    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_phoneno)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val sharedPref = getSharedPreferences("userdetails", Context.MODE_PRIVATE)
        val uid = sharedPref.getString("userid", "haha").toString()

        val username = intent.getStringExtra("username")


        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid)



        val userphonenoTf = findViewById<TextInputLayout>(R.id.tf_userphoneno)
        val usernameTv = findViewById<TextView>(R.id.tv_username)
        val name = "Hey" + " " + username
        usernameTv.text = name
        val nextBtn = findViewById<Button>(R.id.btn_next)

        nextBtn.setOnClickListener {

            val userphoneno = userphonenoTf.editText?.text.toString()

            databaseReference.child("username").setValue(username)
            databaseReference.child("userphoneno").setValue(userphoneno)


            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        val backBtn = findViewById<ImageButton>(R.id.ib_back)
        backBtn.setOnClickListener {
            finish()  // Closes current activity and goes back
        }


    }
}