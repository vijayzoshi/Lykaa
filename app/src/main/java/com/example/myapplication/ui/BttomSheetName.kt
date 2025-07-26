package com.example.myapplication.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.myapplication.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class BttomSheetName : BottomSheetDialogFragment() {


    lateinit var databaseRefrence: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_bttom_sheet_name, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseRefrence = FirebaseDatabase.getInstance().getReference("users")

        val sharedPref = requireActivity().getSharedPreferences("userdetails", Context.MODE_PRIVATE)
        val uid = sharedPref.getString("userid", "haha")


        val usernameTf = view.findViewById<TextInputLayout>(R.id.tf_username)
        usernameTf.editText?.setText("")


        val phonenumberTf = view.findViewById<TextInputLayout>(R.id.tf_phonenumber)
        phonenumberTf.editText?.setText("")

        databaseRefrence.child(uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {


                    usernameTf.editText?.setText(snapshot.child("username").getValue(String::class.java))
                    phonenumberTf.editText?.setText(snapshot.child("userphoneno").getValue(String::class.java))


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })



        val confirmBtn = view.findViewById<Button>(R.id.btn_confirm)
        confirmBtn.setOnClickListener {


            databaseRefrence.child(uid.toString()).child("username").setValue(usernameTf.editText?.text.toString())
            databaseRefrence.child(uid.toString()).child("userphoneno").setValue(phonenumberTf.editText?.text.toString())

            dismiss()
        }







    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}