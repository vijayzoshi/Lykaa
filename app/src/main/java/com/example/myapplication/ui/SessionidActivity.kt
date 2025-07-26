package com.example.myapplication.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivitySessionidBinding
import com.example.myapplication.model.TopExpertsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class SessionidActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySessionidBinding
     var databaseRefrence:  DatabaseReference = FirebaseDatabase.getInstance().getReference()
    private lateinit var sharedPref : SharedPreferences
    lateinit var lat : String
    lateinit var long : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySessionidBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        sharedPref = getSharedPreferences("userdetails", Context.MODE_PRIVATE)
        val uid = sharedPref.getString("userid", "haha")

        val expertid = intent.getIntExtra("expertid",0)
        val sessionid = intent.getIntExtra("sessionid",0)
        val sessionstatus = intent.getStringExtra("sessionstatus")




        binding.myToolbar.title = "Session ID"
        binding.myToolbar.subtitle = sessionid.toString()
        binding.myToolbar.setNavigationOnClickListener {
            finish()
        }
        setSupportActionBar(binding.myToolbar)



        binding.tvHelp.setOnClickListener {

            val intent = Intent(this, HelpActivity :: class.java)
            startActivity(intent)

        }


        getexpertdata(expertid)

        if(sessionstatus == "Upcoming"){
            binding.tvCancelled.visibility = View.GONE
            binding.tvCompleted.visibility = View.GONE
            getsessiondata(uid,"upcomingsesions", sessionid)


        }else{
            getsessiondata(uid,"previoussessions", sessionid)

        }


        binding.btnVisit.setOnClickListener {   // Create the Uri for the location
            val gmmIntentUri = Uri.parse("geo: $lat, $long")

            // Create the implicit Intent
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

            // Set the package to ensure it opens in Google Maps
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent) }






    }

    private fun getsessiondata(uid: String?,sessiontype:String, sessionid: Int) {

        databaseRefrence.child("users").child(uid.toString()).child("sessions").child(sessiontype).child(sessionid.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                binding.tvSessiondate.text =  snapshot.child("sessiondate").getValue<String>()
                binding.tvSessiontime.text =  snapshot.child("sessiontime").getValue<String>()
                binding.tvSessionmode.text =  snapshot.child("sessionmode").getValue<String>()
                if( snapshot.child("sessionmode").getValue<String>() != "Offline"){

                    binding.clinicimage.visibility = View.GONE
                    binding.clinictext.visibility = View.GONE

                    binding.btnVisit.visibility = View.GONE
                    binding.tvClinicaddress.visibility = View.GONE

                }

                if( snapshot.child("sessionstatus").getValue<String>() == "Completed"){

                    binding.tvCancelled.visibility = View.GONE


                }else{
                    binding.tvCompleted.visibility = View.GONE

                }


            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

    private fun getexpertdata(expertid: Int) {
        databaseRefrence.child("experts").child(expertid.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                binding.tvExpertname.text =  snapshot.child("expertname").getValue<String>()
                binding.tvExpertdesign.text =  snapshot.child("expertdesign").getValue<String>()
                binding.tvClinicaddress.text =  snapshot.child("clinic").child("clinic address").getValue<String>()
                lat = snapshot.child("clinic").child("clinic lat").getValue<String>().toString()
                long = snapshot.child("clinic").child("clinic long").getValue<String>().toString()
                binding.tvExpertcharge.text =  snapshot.child("expertcharge").getValue<Int>().toString()

                val imagelink = snapshot.child("expertpic").getValue(String::class.java)
                Glide.with(getApplicationContext())
                    .load(imagelink)
                    .into(binding.ivExpertpic)




            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

    }
}