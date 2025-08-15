package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import com.example.myapplication.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class ProfileActivity : AppCompatActivity() {



    lateinit var faqCardview : CardView
    lateinit var helpCardview : CardView
    lateinit var sessionCardview : CardView
    lateinit var logoutCardview : CardView
    lateinit var usernameTv : TextView
    lateinit var usernumberTv : TextView
    lateinit var editIv : ImageView
    lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        usernameTv = findViewById(R.id.tv_username)
        usernumberTv = findViewById(R.id.tv_usernumber)
        editIv = findViewById(R.id.iv_edit)

        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usernameTv.text =  snapshot.child("1").child("username").getValue<String>()
                usernumberTv.text = "+91 "+snapshot.child("1").child("usernumber").getValue<String>()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        )

        editIv.setOnClickListener {

            val modalBottomSheetDialog = BttomSheetName()
            modalBottomSheetDialog.show(supportFragmentManager, BttomSheetName.TAG)



        }

        faqCardview = findViewById(R.id.cd_faq)
        faqCardview.setOnClickListener {
            val intent = Intent(this@ProfileActivity, FaqActivity::class.java)
            startActivity(intent)
        }


        sessionCardview = findViewById(R.id.cd_session)
        sessionCardview.setOnClickListener {
            val intent = Intent(this@ProfileActivity, SessionActivity::class.java)
            startActivity(intent)
        }


        helpCardview =findViewById(R.id.cd_help)
        helpCardview.setOnClickListener {
            val intent = Intent(this@ProfileActivity, HelpActivity::class.java)
            startActivity(intent)
        }

        logoutCardview = findViewById(R.id.cd_logout)
        logoutCardview.setOnClickListener {

        }

    }
}