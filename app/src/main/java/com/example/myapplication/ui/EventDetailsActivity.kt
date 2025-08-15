/*package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityEventDetailsBinding
import com.example.myapplication.model.EventData

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EventDetailsActivity : AppCompatActivity() {



    private lateinit var recyclerView: RecyclerView
    val database = FirebaseDatabase.getInstance().getReference()
    private lateinit var binding: ActivityEventDetailsBinding
    lateinit var uid: String
    var eventid: Int = 0// Binding class auto-generated


     var eventname: String = ""
     var eventlocation: String = ""
     var eventtime: String = ""
     var eventabout: String = ""
     var eventcharge: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEventDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        eventid = intent.getIntExtra("eventid",0)
        Toast.makeText(this,eventid.toString(), Toast.LENGTH_LONG).show()


  //      val sharedPref = getSharedPreferences("userdetails",MODE_PRIVATE)
  //      uid = sharedPref.getString("userid", "haha").toString()




        if (eventid != null) {
            database.child("allevents").child(eventid.toString())
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        eventname = snapshot.child("eventname").getValue(String::class.java).toString()
                        eventlocation = snapshot.child("eventlocation").getValue(String::class.java).toString()
                        eventtime = snapshot.child("eventtime").getValue(String::class.java).toString()
                        eventabout = snapshot.child("eventabout").getValue(String::class.java).toString()
                        eventcharge = snapshot.child("eventcharge").getValue(Int::class.java)!!

                        binding.tvEventname.text = eventname
                        binding.tvEventlocation.text =eventlocation
                        binding.tvEventtime.text = eventtime
                        binding.tvEventabout.text = eventabout
                        binding.tvEventcharge.text = eventcharge.toString()



                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }
        val bookingid = (100000..999999).random()
        val timestamp = System.currentTimeMillis()

        val event = EventData(

            eventname = eventname,
            eventlocation = eventlocation,
            eventtime = eventtime,
            eventabout = eventabout,
            eventcharge = eventcharge,
            timestamp = timestamp,
            bookingid = bookingid

            )


        binding.btnBook.setOnClickListener {

            database.child("users").child("1").child("myevents").setValue(event)
            val intent : Intent = Intent(this@EventDetailsActivity, EventBookedActivity::class.java)

            startActivity(intent)
            finish()

        }


        }


    }
*/