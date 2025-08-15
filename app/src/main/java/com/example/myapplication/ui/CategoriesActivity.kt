package com.example.myapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.CategoriesAdapter
import com.example.myapplication.databinding.ActivityCategoriesBinding
import com.example.myapplication.model.CategoriesModel

class CategoriesActivity : AppCompatActivity() {

    /*
         lateinit var categoriesRecyclerview : RecyclerView
         lateinit var categoriesAdapter : CategoriesAdapter
         lateinit var categoriesArraylist : ArrayList<CategoriesModel>

     */

    private lateinit var binding: ActivityCategoriesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.toolbar.title = "Faq"
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.cdSession.setOnClickListener {
            val intent = Intent(this@CategoriesActivity, FaqActivity::class.java)
            intent.putExtra("faqtype", "Session")
            startActivity(intent)
        }

        binding.cdPsychologist.setOnClickListener {
            val intent = Intent(this@CategoriesActivity, FaqActivity::class.java)
            intent.putExtra("faqtype", "Psychologist")
            startActivity(intent)
        }

        binding.cdPsychiatrist.setOnClickListener {
            val intent = Intent(this@CategoriesActivity, FaqActivity::class.java)
            intent.putExtra("faqtype", "Psychiatrist")
            startActivity(intent)
        }

        binding.cdListener.setOnClickListener {
            val intent = Intent(this@CategoriesActivity, FaqActivity::class.java)
            intent.putExtra("faqtype", "Listener")
            startActivity(intent)
        }


        /*
          categoriesArraylist = ArrayList<CategoriesModel>()
         categoriesArraylist.add(  CategoriesModel(R.drawable.anxiety, "General"))
         categoriesArraylist.add(  CategoriesModel(R.drawable.adhd, "Session"))
         categoriesArraylist.add(  CategoriesModel(R.drawable.bipolar, "Psychologist"))
         categoriesArraylist.add(  CategoriesModel(R.drawable.love, "Psychiatrist"))
        categoriesArraylist.add(  CategoriesModel(R.drawable.love, "Listener"))
        categoriesAdapter = CategoriesAdapter(this,categoriesArraylist)
                categoriesRecyclerview = findViewById(R.id.rv_categories)
                categoriesRecyclerview.layoutManager =  GridLayoutManager(this,2)
                categoriesRecyclerview.adapter = categoriesAdapter
         */


    }
}