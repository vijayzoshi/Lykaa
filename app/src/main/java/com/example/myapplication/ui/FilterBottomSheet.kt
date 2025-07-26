package com.example.myapplication.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterBottomSheet : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_filter_bottom_sheet, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvSort = view.findViewById<TextView>(R.id.tvSort)
        val tvExpertise = view.findViewById<TextView>(R.id.tvExpertise)
        val tvGender = view.findViewById<TextView>(R.id.tvGender)
        val tvLanguage = view.findViewById<TextView>(R.id.tvLanguage)

        val sortOptions = view.findViewById<RadioGroup>(R.id.sortOptions)
        val expertiseOptions = view.findViewById<LinearLayout>(R.id.expertiseOptions)
        val genderOptions = view.findViewById<RadioGroup>(R.id.genderOptions)
        val languageOptions = view.findViewById<RadioGroup>(R.id.languageOptions)

        fun showOnly(viewToShow: View) {
            listOf(sortOptions, expertiseOptions, genderOptions, languageOptions).forEach {
                it.visibility = if (it == viewToShow) View.VISIBLE else View.GONE
            }
        }

        tvSort.setOnClickListener { showOnly(sortOptions) }
        tvExpertise.setOnClickListener { showOnly(expertiseOptions) }
        tvGender.setOnClickListener { showOnly(genderOptions) }
        tvLanguage.setOnClickListener { showOnly(languageOptions) }


        // 1. Get selected RadioButton (Sort)
        val selectedSortId = sortOptions.checkedRadioButtonId
        val selectedSortText = if (selectedSortId != -1) {
            sortOptions.findViewById<RadioButton>(selectedSortId).text.toString()
        } else null


        var gender: String = ""
        var lang: String = ""
        var sort: String = ""


        genderOptions.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadio = view.findViewById<RadioButton>(checkedId)
            gender = selectedRadio.text.toString()
            Toast.makeText(requireContext(), "Selected: $gender", Toast.LENGTH_SHORT).show()
        }

        languageOptions.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadio = view.findViewById<RadioButton>(checkedId)
            lang = selectedRadio.text.toString()
            Toast.makeText(requireContext(), "Selected: $lang", Toast.LENGTH_SHORT).show()
        }

        sortOptions.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadio = view.findViewById<RadioButton>(checkedId)
            sort = selectedRadio.text.toString()
            Toast.makeText(requireContext(), "Selected: $sort", Toast.LENGTH_SHORT).show()
        }


//        val isanychecked = gender.isNotEmpty() || lang.isNotEmpty() || sort.isNotEmpty()
        view.findViewById<Button>(R.id.btnApply).setOnClickListener {

        /*    (parentFragment as? HomeFragment)?.fetchdatafromfirebase(
                langtype = lang,
                gendertype = gender,
                sorttype = sort
            )
            dismiss()

         */

        }
    }


    companion object {
        const val TAG = "ModalBottomSheet"
    }

}