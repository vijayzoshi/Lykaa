package com.example.myapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.TopExpertAdapter
import com.example.myapplication.model.TopExpertsModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
//import java.util.ArrayList
import java.util.Locale
import kotlin.collections.ArrayList

class AllExpertsActivity : AppCompatActivity() {


    lateinit var expertRecyclerView: RecyclerView
    lateinit var expertAapter: TopExpertAdapter
    lateinit var expertArrayList: ArrayList<TopExpertsModel>
    lateinit var chipPsychologist: Chip
    lateinit var chipPsychiatrist: Chip
    lateinit var chipGroup: ChipGroup
    lateinit var searchEt: EditText
    lateinit var infoIb: ImageButton

    lateinit var filterIb: ImageButton
    lateinit var dailog: BottomSheetDialog


    lateinit var databaseRefrence: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_experts)
/*
        searchEt = findViewById(R.id.et_search)
        searchEt.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

 */



        databaseRefrence = FirebaseDatabase.getInstance().getReference()
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



        val locations = listOf("Delhi", "Mumbai", "Bangalore")
        val adapter = ArrayAdapter(this@AllExpertsActivity, android.R.layout.simple_dropdown_item_1line, locations)
        val locationDropdown = findViewById<AutoCompleteTextView>(R.id.locationDropdown)
        locationDropdown.setAdapter(adapter)
        // Optional: get selected when clicked
        locationDropdown.setOnItemClickListener { parent, _, position, _ ->
            val selected = parent.getItemAtPosition(position).toString()
            Log.d("SelectedLocation", selected)
            Toast.makeText(this@AllExpertsActivity,selected, Toast.LENGTH_LONG).show()

        }





        filterIb = findViewById(R.id.ib_filter)
        filterIb.setOnClickListener {
           // val modalBottomSheetDialog = ModelBottomSheetDialog()
         //   modalBottomSheetDialog.show(supportFragmentManager, ModelBottomSheetDialog.TAG)

             val filterBottomSheetDialog = FilterBottomSheet()
             filterBottomSheetDialog.show(supportFragmentManager, FilterBottomSheet.TAG)

        }

        chipGroup = findViewById(R.id.chipGroup)
        chipPsychologist = findViewById(R.id.cp_psychologist)
        chipPsychiatrist = findViewById(R.id.cp_psychiatrist)

        chipPsychologist.setOnClickListener {
         //   filter("psychologist")
            fetchdatafromfirebase("Psychologist")

        }

        chipPsychiatrist.setOnClickListener {
           // filter("psychiatrist")
            fetchdatafromfirebase("Psychiatrist")

        }


        val checkedChipId = chipGroup.checkedChipId // Returns View.NO_ID if singleSelection = false

        chipGroup.setOnCheckedStateChangeListener { group, checkedId ->



        }





        expertRecyclerView = findViewById(R.id.rv_allexperts)
        expertRecyclerView.layoutManager = LinearLayoutManager(this)
        expertArrayList = ArrayList<TopExpertsModel>()
        expertAapter = TopExpertAdapter(this@AllExpertsActivity, expertArrayList)
        expertRecyclerView.adapter = expertAapter

      fetchdatafromfirebase("Psychologist")

    }

    fun fetchdatafromfirebase(  designtype : String, sorttype : String = "", langtype : String = "", gendertype : String = "", experttype : String = "") {

        databaseRefrence.child("experts").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                expertArrayList.clear()


                if (snapshot.exists()) {

                    for (datasnapshot in snapshot.children) {
                        val data = datasnapshot.getValue(TopExpertsModel::class.java)
                        if (data != null  && data.expertdesign == designtype ) {

                         if(   data.expertgender == gendertype)

                            {  expertArrayList.add(data)
                            }else if(data.expertlang.contains(langtype)){
                              expertArrayList.add(data)

                         }




/*
                            if( data.expertlang.contains(langtype) && data.expertgender == gendertype)

                            {  expertArrayList.add(data)
                            }


 */


                        }

                    }

                }


             when (sorttype) {
                    "Price: High to Low" ->  expertArrayList.sortByDescending { it.expertcharge }
                    "Exp: High to Low" -> expertArrayList.sortByDescending { it.expertexp }
                    "Price: Low to High"-> expertArrayList.sortBy { it.expertcharge }
                    "Exp: Low to High" -> expertArrayList.sortBy { it.expertexp }
                }



                /*
                if(pricesorthightolow == "Price: High to Low"){
                    expertArrayList.sortByDescending { it.expertcharge }
                }
                if(expsorthightolow == "Exp: High to Low"){
                    expertArrayList.sortByDescending { it.expertexp }
                }

                if(pricesortlowtohigh == "Price: Low to High"){
                    expertArrayList.sortBy { it.expertcharge }
                }
                if(expsortlowtohigh == "Exp: Low to High"){
                    expertArrayList.sortBy { it.expertexp }
                }


                 */




                expertAapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


    }

/*

    fun filter(word: String) {
        val searchList = ArrayList<TopExpertsModel>()
        for (x in expertArrayList) {
            if (x.expertdesign.lowercase().contains(word.lowercase(Locale.getDefault()))
            ) {
                searchList.add(x)
            }
        }
        expertAapter.searchDataList(searchList)
    }

 */

}