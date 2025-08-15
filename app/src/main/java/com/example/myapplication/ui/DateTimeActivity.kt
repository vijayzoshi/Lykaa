package com.example.myapplication.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.model.TopExpertsModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class DateTimeActivity : AppCompatActivity() {


    private lateinit var databaseReference: DatabaseReference
    var selecteddate: String? = null
    var selectedtime: String? = null
    var sessionmode: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_time)
        val toolbar = findViewById<MaterialToolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        val expertid = intent.getIntExtra("expertid", 0)
        val expertidtype = intent.getIntExtra("expertidtype", 0)


        val nextBtn = findViewById<Button>(R.id.btn_next)

        val sessionCg = findViewById<ChipGroup>(R.id.cg_session)

        val audio = findViewById<Chip>(R.id.chip_audio)
        val online = findViewById<Chip>(R.id.chip_online)
        val offline = findViewById<Chip>(R.id.chip_offline)

        if (expertidtype == 3) {
            audio.visibility = View.VISIBLE
            online.visibility = View.GONE
            offline.visibility = View.GONE
            audio.isChecked = true
            sessionmode = "Audio Call"
        }else{
            audio.visibility = View.GONE
            online.visibility = View.VISIBLE
            offline.visibility = View.VISIBLE

            sessionCg.setOnCheckedChangeListener { group, checkedId ->
                if (checkedId != View.NO_ID) {
                    val selectedChip = group.findViewById<Chip>(checkedId)
                    sessionmode = selectedChip.text.toString()
                }
            }
        }





        val availabledatesCg = findViewById<ChipGroup>(R.id.cg_availabledates)
        val availabletimesCg = findViewById<ChipGroup>(R.id.cg_availabletimes)



        val timeslotTv = findViewById<TextView>(R.id.tv_timeslot)
        val bookedTv = findViewById<TextView>(R.id.tv_booked)
        val bookedIv = findViewById<ImageView>(R.id.iv_booked)
        bookedTv.visibility = View.GONE
        timeslotTv.visibility = View.GONE
        bookedIv.visibility  = View.GONE





        databaseReference =
            FirebaseDatabase.getInstance().getReference("experts").child(expertid.toString())
                .child("availability")


        val datearraylist = ArrayList<String>()

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    for (dateSnapshot in snapshot.children) {
                        dateSnapshot.key?.let { datearraylist.add(it) }
                    }

                    val formatter = SimpleDateFormat("dd MMMM", Locale.ENGLISH)
                    datearraylist.sortWith(compareBy { formatter.parse(it) })

                    for (date in datearraylist) {
                        val chip = Chip(this@DateTimeActivity)
                        chip.text = date
                        chip.isCheckable = true
                        chip.isClickable = true

                      //  chip.setChipBackgroundColorResource(R.color.chip_selector)

                        chip.setTextColor(Color.BLACK)
                        chip.chipStrokeColor = ColorStateList.valueOf(Color.WHITE)
                        availabledatesCg.addView(chip)
                    }



                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DateTimeActivity, "Error: ${error.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })


        availabledatesCg.setOnCheckedChangeListener { group, checkedId ->
            availabletimesCg.removeAllViews()
            if (checkedId != View.NO_ID) {
                val selectedChip = group.findViewById<Chip>(checkedId)

                selecteddate = selectedChip.text.toString()
                timeslotTv.visibility = View.VISIBLE
                bookedTv.visibility = View.VISIBLE
                bookedIv.visibility  = View.VISIBLE


                databaseReference.child(selecteddate!!).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val slotMap = mutableListOf<Pair<String, String>>()

                        for (slotSnapshot in snapshot.children) {
                            val time = slotSnapshot.key
                            val status = slotSnapshot.getValue(String::class.java)

                            if (!time.isNullOrEmpty()) {
                                slotMap.add(Pair(time, status ?: ""))
                            }
                        }

                        // Sort by start time (11:00 AM in "11:00 AM - 12:00 PM")
                        val formatter = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
                        slotMap.sortBy { pair ->
                            val startTime = pair.first.split(" - ").firstOrNull()
                            formatter.parse(startTime ?: "12:00 AM")
                        }

                        // Now show sorted chips
                        for ((time, status) in slotMap) {
                            val timeChip = Chip(this@DateTimeActivity).apply {
                                text = time
                                isCheckable = status != "b"
                                isClickable = status != "b"
                                setTextColor(Color.BLACK)

                                chipStrokeColor = ColorStateList.valueOf(Color.WHITE)

                                if (status == "b") {
                                    chipBackgroundColor = ColorStateList.valueOf(Color.rgb(254, 180, 180)) // Red for booked
                                }
                            }

                            availabletimesCg.addView(timeChip)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })


                // Load Time Slots
                /*
                databaseReference.child(selecteddate!!).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (slotSnapshot in snapshot.children) {
                                val time = slotSnapshot.key
                                val status = slotSnapshot.getValue(String::class.java)

                                if (status == "b" && !time.isNullOrEmpty()) {
                                    val timeChip = Chip(this@DateTimeActivity).apply {
                                        text = time
                                        isCheckable = false
                                        isClickable = false
                                        chipBackgroundColor = ColorStateList.valueOf(Color.rgb(255, 138, 138))


                                        setTextColor(Color.BLACK)
                                    }
                                    availabletimesCg.addView(timeChip)

                                } else {
                                    val timeChip = Chip(this@DateTimeActivity).apply {
                                        text = time
                                        isCheckable = true
                                        isClickable = true


                                        setTextColor(Color.BLACK)
                                    }
                                    availabletimesCg.addView(timeChip)
                                }


                            }
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })

                */
            }
        }



        availabletimesCg.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != View.NO_ID) {
                val selectedChip = group.findViewById<Chip>(checkedId)
                selectedtime = selectedChip.text.toString()
            }
        }





        nextBtn.setOnClickListener {
            val intent = Intent(this, SessionDetailsActivity::class.java)
            intent.putExtra("expertid", expertid)
            intent.putExtra("sessiondate", selecteddate)
            intent.putExtra("sessiontime", selectedtime)
            intent.putExtra("sessionmode", sessionmode)
            startActivity(intent)
        }


    }
}