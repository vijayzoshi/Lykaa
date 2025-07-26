package com.example.myapplication.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.EventAdapter
import com.example.myapplication.model.EventsModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class ProfileFragment : Fragment(R.layout.fragment_profile) {

/*
    lateinit var recyclerView: RecyclerView
    var eventsArraylist = ArrayList<EventsModel>()
    lateinit var adapter: EventAdapter
    val dbRef =  FirebaseDatabase.getInstance().getReference()


  //  lateinit var uid: String

 */
private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var sharedPref : SharedPreferences


    lateinit var faqCardview : CardView
    lateinit var helpCardview : CardView
    lateinit var sessionCardview : CardView
    lateinit var logoutCardview : CardView
    lateinit var usernameTv : TextView
    lateinit var usernumberTv : TextView
    lateinit var editIv : ImageView
    lateinit var databaseReference: DatabaseReference
    override fun
            onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        sharedPref = requireActivity().getSharedPreferences("userdetails", Context.MODE_PRIVATE)
        val uid = sharedPref.getString("userid", "haha")

        usernameTv = view.findViewById(R.id.tv_username)
        usernumberTv = view.findViewById(R.id.tv_usernumber)
        editIv = view.findViewById(R.id.iv_edit)

        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usernameTv.text =  snapshot.child(uid.toString()).child("username").getValue<String>()
                usernumberTv.text = snapshot.child(uid.toString()).child("userphoneno").getValue<String>()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        )

        editIv.setOnClickListener {

            val modalBottomSheetDialog = BttomSheetName()
            modalBottomSheetDialog.show(parentFragmentManager, BttomSheetName.TAG)



        }

        faqCardview = view.findViewById(R.id.cd_faq)
        faqCardview.setOnClickListener {
            val intent = Intent(activity, CategoriesActivity::class.java)
            startActivity(intent)
        }


        sessionCardview = view.findViewById(R.id.cd_session)
        sessionCardview.setOnClickListener {
            val intent = Intent(activity, SessionActivity::class.java)
            startActivity(intent)
        }


        helpCardview =view.findViewById(R.id.cd_help)
        helpCardview.setOnClickListener {
            val intent = Intent(activity, HelpActivity::class.java)
            startActivity(intent)
        }


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))  // From Firebase
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        logoutCardview = view.findViewById(R.id.cd_logout)
        logoutCardview.setOnClickListener {

            signOut()
        }

        /*
              //  val sharedPref = getSharedPreferences("userdetails", Context.MODE_PRIVATE)
             //   uid = sharedPref.getString("userid", "haha").toString()




                recyclerView = view.findViewById(R.id.rv_allevents)
                adapter = EventAdapter(requireContext(),eventsArraylist)

                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = adapter

                fetchalleventsFromFirebase()

                val locations = listOf("Delhi", "Mumbai", "Bangalore")
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, locations)
                val locationDropdown = view.findViewById<AutoCompleteTextView>(R.id.locationDropdown)
                locationDropdown.setAdapter(adapter)

        // Optional: get selected when clicked
                locationDropdown.setOnItemClickListener { parent, _, position, _ ->
                    val selected = parent.getItemAtPosition(position).toString()
                    Log.d("SelectedLocation", selected)
                    Toast.makeText(requireContext(),selected, Toast.LENGTH_LONG).show()

                }





            }

            private fun fetchalleventsFromFirebase() {


                dbRef.child("allevents").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        eventsArraylist.clear()
                        for (child in snapshot.children) {
                            val student = child.getValue(EventsModel::class.java)
                            if (student != null ) {
                                eventsArraylist.add(student)
                            }
                        }

                       // eventsArraylist.sortByDescending { it.timestamp }
                        adapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })


         */

    }

    private fun signOut() {
        // Firebase sign out
        FirebaseAuth.getInstance().signOut()

        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(requireActivity()) {
            Toast.makeText(requireContext(), "Signed out successfully", Toast.LENGTH_SHORT).show()
            sharedPref.edit().remove("userid").apply()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)



            // Optional: navigate to login screen or update UI
        }
    }

}