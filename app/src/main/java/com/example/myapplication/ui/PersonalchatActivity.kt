package com.example.myapplication.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.PersonalchatsAdapter
//import com.example.myapplication.model.EventsModel
import com.example.myapplication.model.PersonalchatsModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
// import kotlin.coroutines.jvm.internal.CompletedContinuation.context

class PersonalchatActivity : AppCompatActivity() {


    lateinit var sendmsgEt: EditText
    lateinit var sendIb: ImageButton
    lateinit var databaseReference: DatabaseReference
    lateinit var senderid : String
    lateinit var personalchatRecyclerview: RecyclerView
    lateinit var personalchatArraylist : ArrayList<PersonalchatsModel>
    lateinit var personalchatAdapter: PersonalchatsAdapter
    lateinit var toolbar : MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalchat)

        personalchatRecyclerview = findViewById(R.id.rv_personalchats)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val sharedPref = getSharedPreferences("userdetails", Context.MODE_PRIVATE)
        val uid = sharedPref.getString("userid", "haha").toString()


        val expertid = intent.getIntExtra("expertid",0).toString()
        val expertname = intent.getStringExtra("expertname")
        toolbar.title = expertname
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val infoIv = findViewById<ImageButton>(R.id.tv_book)
        infoIv.setOnClickListener {

            val modalBottomSheetDialog = BottomSheetInfo()
            modalBottomSheetDialog.show(supportFragmentManager, BottomSheetInfo.TAG)

        }


                //   senderid = FirebaseAuth.getInstance().uid.toString()
        senderid = uid

        sendmsgEt = findViewById(R.id.et_sendmsg)
        sendIb = findViewById(R.id.ib_send)
        databaseReference =
            FirebaseDatabase.getInstance().getReference("users").child(uid).child("expertschat")
                .child(expertid)

        val timestamp = System.currentTimeMillis()

        sendIb.setOnClickListener {

            if (sendmsgEt.text.toString().isNotEmpty()) {
                val data = PersonalchatsModel(timestamp,senderid, sendmsgEt.text.toString(), getcurrenttime())
                databaseReference.child("msgs").push().setValue(data)
                sendmsgEt.text.clear()

            }

        }/**/

        val test = findViewById<ImageButton>(R.id.ib_test)
        test.setOnClickListener{

            val data = PersonalchatsModel(timestamp,expertid, sendmsgEt.text.toString(), getcurrenttime())
            databaseReference.child("msgs").push().setValue(data)
            sendmsgEt.text.clear()
        }
        personalchatArraylist = ArrayList<PersonalchatsModel>()
        personalchatRecyclerview.layoutManager= LinearLayoutManager(this@PersonalchatActivity)
        personalchatAdapter = PersonalchatsAdapter( this, personalchatArraylist)
        personalchatRecyclerview.adapter =  personalchatAdapter

        databaseReference.child("msgs").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                personalchatArraylist.clear()
                if(snapshot.exists()){

                    for(datasnapshot in snapshot.children){

                        val data = datasnapshot.getValue(PersonalchatsModel:: class.java)
                        personalchatArraylist.add(data!!)
                    }


                }
                personalchatArraylist.sortBy { it.timestamp }

                personalchatAdapter.notifyDataSetChanged()
               personalchatRecyclerview.scrollToPosition(personalchatArraylist.size - 1)


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })


    }

    private fun getcurrenttime(): String {



        val istZone = ZoneId.of("Asia/Kolkata")
        val currentISTTime = ZonedDateTime.now(istZone)
        val ordertime = currentISTTime.format(DateTimeFormatter.ofPattern("hh:mm a, dd MMM")).toString()
        return ordertime
    }

}