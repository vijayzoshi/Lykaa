package com.example.myapplication.ui

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
import com.example.myapplication.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig

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
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid)



        val username = intent.getStringExtra("username")






        val userphonenoTf = findViewById<TextInputLayout>(R.id.tf_userphoneno)
        val usernameTv = findViewById<TextView>(R.id.tv_username)
        val nextBtn = findViewById<Button>(R.id.btn_next)

        usernameTv.text = "Hey" + " " + username



        val  application = application // Android's application context
        val  appID =1128845963L    // yourAppID
        val appSign ="7ed70bc4ef4935a75f27ed561414f69f26bde8e574fec4b505b1ec47b6397eb3";  // yourAppSign



        nextBtn.setOnClickListener {

            val userphoneno = userphonenoTf.editText?.text.toString()

            val userID = uid
            val userName  =  username


            val callInvitationConfig =  ZegoUIKitPrebuiltCallInvitationConfig();
            ZegoUIKitPrebuiltCallService.init(
                application,
                appID,
                appSign,
                userID,
                userName,
                callInvitationConfig);

            databaseReference.child("username").setValue(username)
            databaseReference.child("userphoneno").setValue(userphoneno)


            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)


        }



        val backBtn = findViewById<ImageButton>(R.id.ib_back)
        backBtn.setOnClickListener {
            finish()
        }


    }
}