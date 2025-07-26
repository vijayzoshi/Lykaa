package com.example.myapplication.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivitySessionDetailsBinding
import com.example.myapplication.model.SessionData
import com.example.myapplication.model.TopExpertsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlin.random.Random
import java.text.SimpleDateFormat
import java.util.*


class SessionDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySessionDetailsBinding
    lateinit var databaseRefrence: DatabaseReference
    lateinit var databaseRefrenceuser: DatabaseReference
    private lateinit var sharedPref : SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySessionDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = getSharedPreferences("userdetails", Context.MODE_PRIVATE)
        val uid = sharedPref.getString("userid", "haha")

        setSupportActionBar(binding.myToolbar)
        binding.myToolbar.setNavigationOnClickListener() {
            // Handle navigation icon press
            finish()

        }

        val expertid = intent.getIntExtra("expertid",0)
        val sessiondate = intent.getStringExtra("sessiondate").toString()
        val sessiontime = intent.getStringExtra("sessiontime").toString()
   //     val patientname = intent.getStringExtra("patientname").toString()
        val sessionmode = intent.getStringExtra("sessionmode").toString()

     //   binding.tvPatientname.text = patientname
        binding.tvSessionmode.text = sessionmode
        binding.tvSessiondate.text= sessiondate
        binding.tvSessiontime.text = sessiontime



        databaseRefrence = FirebaseDatabase.getInstance().getReference("experts")

        databaseRefrence.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val expert = snapshot.child(expertid.toString()).getValue<TopExpertsModel>()
                if (expert != null) {
                    binding.tvExpertname.text = expert.expertname
                    binding.tvExpertdesign.text = expert.expertdesign
                    val   imagelink = expert.expertpic
                    Glide.with(getApplicationContext())
                        .load(imagelink)
                        .into(binding.ivExpertpic)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })


        databaseRefrenceuser = FirebaseDatabase.getInstance().getReference("users").child(uid.toString())


    //    val timestamp = System.currentTimeMillis()
        val date = sessiondate
        val time = sessiontime.take(8)
        val year = Calendar.getInstance().get(Calendar.YEAR)

        val fullDateTime = "$date $year $time"  // e.g. "19 June 2025 10:00 AM"

        val format = SimpleDateFormat("dd MMMM yyyy hh:mm a", Locale.ENGLISH)
        val parsedDate = format.parse(fullDateTime)
        val timestampnew = parsedDate?.time ?: 0L

        binding.btnConfirm.setOnClickListener {

            val sessionid = sessionId()
            val sessiondata = SessionData("Upcoming",timestampnew,expertid, sessiondate,sessiontime,  sessionmode, sessionid)

            databaseRefrenceuser.child("sessions").child("upcomingsesions").child(sessionid.toString()).setValue(sessiondata)
            val intent = Intent(this, BookingConfirmedActivity:: class.java)
            startActivity(intent)
            finish()
        }


    }

    fun sessionId(): Int {
        return Random.nextInt(100000, 1000000) // Generates a number from 100000 to 999999
    }



}