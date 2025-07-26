package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.myapplication.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ModelBottomSheetDialog : BottomSheetDialogFragment() {



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.bottomsheet_info, container, false)





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        val pricelowtohighBtn = view.findViewById<Button>(R.id.pricelowtohigh)
        pricelowtohighBtn.setOnClickListener {


            (activity as? AllExpertsActivity)?.fetchdatafromfirebase("Psychologist", pricesortlowtohigh = "lowtohigh")

            dismiss()
        }



        val pricehightolowBtn = view.findViewById<Button>(R.id.pricehightolow)
        pricehightolowBtn.setOnClickListener {


            (activity as? AllExpertsActivity)?.fetchdatafromfirebase("Psychologist", pricesorthightolow = "hightolow")

            dismiss()
        }

       val explowtohighBtn = view.findViewById<Button>(R.id.explowtohigh)
        pricelowtohighBtn.setOnClickListener {


            (activity as? AllExpertsActivity)?.fetchdatafromfirebase("Psychologist",expsortlowtohigh = "lowtohigh")

            dismiss()
        }



        val exphightolowBtn = view.findViewById<Button>(R.id.exphightolow)
        exphightolowBtn.setOnClickListener {


            (activity as? AllExpertsActivity)?.fetchdatafromfirebase("Psychologist", expsorthightolow = "hightolow")

            dismiss()
        }
        val maleBtn = view.findViewById<Button>(R.id.male)
        maleBtn.setOnClickListener {


            (activity as? AllExpertsActivity)?.fetchdatafromfirebase("Psychologist", gendertype = "male")

            dismiss()
        }
        val femaleBtn = view.findViewById<Button>(R.id.female)
        femaleBtn.setOnClickListener {


            (activity as? AllExpertsActivity)?.fetchdatafromfirebase("Psychologist", gendertype = "female")

            dismiss()
        }
*/

    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

}