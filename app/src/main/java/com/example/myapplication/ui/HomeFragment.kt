package com.example.myapplication.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.example.myapplication.adapter.TopExpertAdapter
import com.example.myapplication.model.TopExpertsModel
import com.example.myapplication.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import java.util.ArrayList


class HomeFragment : Fragment(R.layout.fragment_home) {


    lateinit var expertRecyclerView: RecyclerView
    lateinit var expertAapter: TopExpertAdapter
    lateinit var expertArrayList: ArrayList<TopExpertsModel>
    lateinit var chipPsychologist: Chip
    lateinit var chipPsychiatrist: Chip
    lateinit var chipListener: Chip
    lateinit var chipGroup: ChipGroup
    lateinit var searchEt: EditText
    lateinit var infoIb: ImageButton

    lateinit var filterIb: Button
    lateinit var dailog: BottomSheetDialog


    var databaseRefrence: DatabaseReference = FirebaseDatabase.getInstance().getReference()

    lateinit var appointmentcl: CardView

    //  lateinit var homePi: CircularProgressIndicator
    lateinit var expertnameTv: TextView
    lateinit var sessiontimeTv: TextView
    lateinit var sessiondateTv: TextView
    lateinit var expertdesignTv: TextView
    lateinit var joinsessionBtn: Button
    lateinit var nextsessiomTv: TextView

 var location : String = "a"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //  homePi = view.findViewById(R.id.pi_home)
        appointmentcl = view.findViewById(R.id.cl)
        expertnameTv = view.findViewById(R.id.tv_expertname)
        sessiontimeTv = view.findViewById(R.id.tv_time)
        sessiondateTv = view.findViewById(R.id.tv_date)
        expertdesignTv = view.findViewById(R.id.tv_designation)
        joinsessionBtn = view.findViewById(R.id.btn_joinsession)

        nextsessiomTv = view.findViewById<TextView>(R.id.nextsession)
        nextsessiomTv.visibility = View.GONE
        appointmentcl.visibility = View.GONE


        // Search
        val expertEt: EditText = view.findViewById(R.id.et_search)
        expertEt.setOnClickListener {
            val intent = Intent(activity, SearchActivity::class.java)
            startActivity(intent)
        }


        //homePi.show()
        getnextsession();


        /*
               infoIb = findViewById(R.id.ib_info)
                infoIb.setOnClickListener {
                 MaterialAlertDialogBuilder(this)
                          .setTitle("Do Yo Know?")
                          .setMessage(
                                  "A psychologist focuses on mental health through therapy and behavioral interventions, holding a doctoral degree in psychology (Ph.D. or Psy.D.), but typically cannot prescribe medications. In contrast, a psychiatrist is a medical doctor (M.D. or D.O.) who can diagnose mental health disorders and prescribe medications, often treating patients with a combination of medication and therapy. Psychologists handle emotional and cognitive aspects, while psychiatrists manage the medical and biological components of mental health conditions.")

                          .setPositiveButton("Got it") { dialog, which ->
                              filter("psychologist")

                              dialog.dismiss()
                          }
                          .show()



                }

         */


        val locations = listOf("All","Delhi", "Mumbai", "Bangalore")
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, locations)
        val locationDropdown = view.findViewById<AutoCompleteTextView>(R.id.locationDropdown)
        locationDropdown.setAdapter(adapter)
        locationDropdown.setOnItemClickListener { parent, _, position, _ ->
            location = parent.getItemAtPosition(position).toString()
            Toast.makeText(requireContext(), location, Toast.LENGTH_LONG).show()
            if (location == "All") {
                location = "a"  // Reset to default unfiltered marker
            }

            when {
                chipPsychologist.isChecked -> fetchdatafromfirebase("Psychologist")
                chipPsychiatrist.isChecked -> fetchdatafromfirebase("Psychiatrist")
                chipListener.isChecked -> fetchdatafromfirebase("Listener")
            }

        }





  /*      filterIb = view.findViewById(R.id.ib_filter)
        filterIb.setOnClickListener {

            val filterBottomSheetDialog = FilterBottomSheet()
            filterBottomSheetDialog.show(parentFragmentManager, FilterBottomSheet.TAG)

        }

   */

      //  chipGroup = view.findViewById(R.id.chipGroup)
        chipPsychologist = view.findViewById(R.id.cp_psychologist)
        chipPsychiatrist = view.findViewById(R.id.cp_psychiatrist)
        chipListener = view.findViewById(R.id.cp_listener)


        val infoTv = view.findViewById<TextView>(R.id.tv_info)
        val learnTv = view.findViewById<TextView>(R.id.tv_learnmore)



        lateinit var faqtype: String
        chipPsychologist.setOnClickListener {
            //   filter("psychologist")
            infoTv.text = "Help you with therapy based approach."
            fetchdatafromfirebase("Psychologist")
            faqtype = "Psychologist"

        }

        chipPsychiatrist.setOnClickListener {
            // filter("psychiatrist")
            infoTv.text = "Help you with medication based approach."

            fetchdatafromfirebase("Psychiatrist")
            faqtype = "Psychiatrist"


        }

        chipListener.setOnClickListener {
            // filter("psychiatrist")
            infoTv.text = "Help you with listening based approach."

            fetchdatafromfirebase("Listener")
            faqtype = "Listener"



        }
        learnTv.setOnClickListener {
            val intent = Intent(activity, FaqActivity::class.java)
            intent.putExtra("faqtype", faqtype)
            startActivity(intent)
        }







        expertRecyclerView = view.findViewById(R.id.rv_allexperts)
        expertRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        expertArrayList = ArrayList<TopExpertsModel>()
        expertAapter = TopExpertAdapter(requireContext(), expertArrayList)
        expertRecyclerView.adapter = expertAapter

        fetchdatafromfirebase("Psychologist")


    }


    fun getnextsession() {

        databaseRefrence.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                //homePi.hide()


                val issessionbooked =
                    snapshot.child("users").child("1").child("nextsession").child("isbooked")
                        .getValue<Boolean>()!!

                if (issessionbooked) {
                    // Upcomingsession

                    //   val expertpicIv : ImageView = view.findViewById(R.id.iv_expertpic)
                    appointmentcl.visibility = View.VISIBLE
                    nextsessiomTv.visibility = View.VISIBLE


                    expertnameTv.text =
                        snapshot.child("users").child("1").child("nextsession")
                            .child("nextsessiondetails")
                            .child("expertname").getValue<String>()
                    sessiondateTv.text =
                        snapshot.child("users").child("1").child("nextsession")
                            .child("nextsessiondetails")
                            .child("sessiondate").getValue<String>()
                    sessiontimeTv.text =
                        snapshot.child("users").child("1").child("nextsession")
                            .child("nextsessiondetails")
                            .child("sessiontime").getValue<String>()
                    expertdesignTv.text =
                        snapshot.child("users").child("1").child("nextsession")
                            .child("nextsessiondetails")
                            .child("expertdesign").getValue<String>()

                    val isonlineSession =
                        snapshot.child("users").child("1").child("nextsession")
                            .child("nextsessiondetails")
                            .child("isonlinesession").getValue<Boolean>()!!

                    if (isonlineSession) {
                        joinsessionBtn.text = "Join Session"
                        joinsessionBtn.setOnClickListener {
                            // startmyservice("***********")
                            val intent = Intent(activity, NewActivity::class.java)
                            startActivity(intent)
                        }
                    } else {


                        val lat =
                            snapshot.child("users").child("1").child("nextsession")
                                .child("nextsessiondetails")
                                .child("cliniclat").getValue<String>()
                        val long =
                            snapshot.child("users").child("1").child("nextsession")
                                .child("nextsessiondetails")
                                .child("cliniclong").getValue<String>()



                        joinsessionBtn.text = "Clinic Address"
                        joinsessionBtn.setOnClickListener {

                            // Create the Uri for the location
                            //    val gmmIntentUri = Uri.parse("geo:28.6129,77.2295")
                            val gmmIntentUri = Uri.parse("geo:$lat,$long")

                            // Create the implicit Intent
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

                            // Set the package to ensure it opens in Google Maps
                            mapIntent.setPackage("com.google.android.apps.maps")
                            startActivity(mapIntent)

                        }
                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        }


        )


    }


    fun fetchdatafromfirebase(
        designtype: String
    /*
        sorttype: String = "",
        langtype: String = "",
        gendertype: String = "",
        experttype: String = ""
        */
    ) {

        databaseRefrence.child("experts").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                expertArrayList.clear()


                if (snapshot.exists()) {

                    for (datasnapshot in snapshot.children) {
                        val data = datasnapshot.getValue(TopExpertsModel::class.java)
                        if (data != null && data.expertdesign == designtype ) {

                            if(location == "a"){
                                expertArrayList.add(data)

                            }else{
                                if (data.expertmode.contains(location)) {
                                    expertArrayList.add(data)
                                }
                            }



/*
                            if (data.expertgender == gendertype) {
                                expertArrayList.add(data)
                            } else if (data.expertlang.contains(langtype)) {
                                expertArrayList.add(data)

                            }
*/
                            /*
                                                        if( data.expertlang.contains(langtype) && data.expertgender == gendertype)

                                                        {  expertArrayList.add(data)
                                                        }


                             */


                        }

                    }

                }

/*
                when (sorttype) {
                    "Price: High to Low" -> expertArrayList.sortByDescending { it.expertcharge }
                    "Exp: High to Low" -> expertArrayList.sortByDescending { it.expertexp }
                    "Price: Low to High" -> expertArrayList.sortBy { it.expertcharge }
                    "Exp: Low to High" -> expertArrayList.sortBy { it.expertexp }
                }


 */


                expertAapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


      //  Toast.makeText(context, sorttype,Toast.LENGTH_LONG).show()

    }


}