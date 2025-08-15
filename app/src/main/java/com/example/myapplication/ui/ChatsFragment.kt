package com.example.myapplication.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.AllchatsAdapter
import com.example.myapplication.model.AllChatsmodel
import com.example.myapplication.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatsFragment : Fragment(R.layout.fragment_chats) {



    lateinit var allchatRecyclerview: RecyclerView
    lateinit var allchatArraylist : ArrayList<AllChatsmodel>
    lateinit var allchatAdapter: AllchatsAdapter
    lateinit var databaseRefrence : DatabaseReference
    private lateinit var sharedPref : SharedPreferences


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allchatRecyclerview = view.findViewById(R.id.rv_allchats)
        allchatRecyclerview.layoutManager= LinearLayoutManager(context)
        allchatArraylist = ArrayList<AllChatsmodel>()
        val nochat = view.findViewById<TextView>(R.id.nochatyet)
        val booked = view.findViewById<TextView>(R.id.booked)



        sharedPref = requireActivity().getSharedPreferences("userdetails", Context.MODE_PRIVATE)
        val uid = sharedPref.getString("userid", "haha")

        databaseRefrence = FirebaseDatabase.getInstance().getReference("users").child(uid.toString()).child("expertschat")
        databaseRefrence.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                allchatArraylist.clear()

                if(snapshot.exists()){
                    allchatRecyclerview.visibility = View.VISIBLE

                    for(datasnapshot in snapshot.children){

                        val data = datasnapshot.getValue(AllChatsmodel:: class.java)
                        allchatArraylist.add(data!!)
                    }
                    allchatAdapter = AllchatsAdapter(context!!, view,allchatArraylist)
                    allchatRecyclerview.adapter = allchatAdapter

                } else{
                nochat.visibility = View.VISIBLE
                    booked.visibility = View.VISIBLE


                }

        }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })























    }

}