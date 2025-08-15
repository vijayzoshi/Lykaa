package com.example.myapplication.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.UpcomingSessionAdapter
import com.example.myapplication.model.UpcomingSessionModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CompletesessionFragment : Fragment() {


    lateinit var upcomingsessionRecyclerview: RecyclerView
    lateinit var upcomingsessionArraylist: ArrayList<UpcomingSessionModel>
    lateinit var upcomingsessionAdapter: UpcomingSessionAdapter
    lateinit var databaseRefrence: DatabaseReference
    lateinit var nosessionLinearLayout: LinearLayout
    private lateinit var sharedPref : SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_completedsession, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = requireActivity().getSharedPreferences("userdetails", Context.MODE_PRIVATE)
        val uid = sharedPref.getString("userid", "haha")
        nosessionLinearLayout = view.findViewById(R.id.ll_nosession)

        upcomingsessionRecyclerview = view.findViewById(R.id.rv_upcomingsession)
        upcomingsessionRecyclerview.layoutManager = LinearLayoutManager(context)
        upcomingsessionArraylist = ArrayList<UpcomingSessionModel>()
        upcomingsessionAdapter =
            UpcomingSessionAdapter("previous", requireContext(), upcomingsessionArraylist)
        upcomingsessionRecyclerview.adapter = upcomingsessionAdapter

        databaseRefrence =
            FirebaseDatabase.getInstance().getReference("users").child(uid.toString())
                .child("sessions").child("previoussessions")
        databaseRefrence.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                upcomingsessionArraylist.clear()


                if (snapshot.exists()) {

                    upcomingsessionRecyclerview.visibility = View.VISIBLE
                    for (datasnapshot in snapshot.children) {
                        val data = datasnapshot.getValue(UpcomingSessionModel::class.java)
                        if (data != null && data.sessionstatus != "Upcoming") {
                            upcomingsessionArraylist.add(data)
                        }
                    }



                } else {
                    nosessionLinearLayout.visibility = View.VISIBLE

                }








                upcomingsessionArraylist.sortBy { it.timestamp }

                upcomingsessionAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}