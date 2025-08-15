package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.ui.ExpertprofileActivity
import com.example.myapplication.model.TopExpertsModel
import com.example.myapplication.R

class TopExpertAdapter(val context: Context, var datalist : ArrayList<TopExpertsModel>) : RecyclerView.Adapter<TopExpertAdapter.MyViewHolder>() {



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.rv_experts, parent, false)
        return MyViewHolder(view)

    }


    

    override fun getItemCount(): Int {
        return datalist.size
    }

    fun searchDataList(searchList: ArrayList<TopExpertsModel>) {
        datalist = searchList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.expertname.text = datalist.get(position).expertname
        holder.expertmode.text = datalist.get(position).expertmode
        holder.expertage.text = datalist.get(position).expertage



        val designation = datalist.get(position).expertdesign
        holder.expertdesign.text = designation

        holder.expertlang.text = datalist.get(position).expertlang


        holder.expertexp.text =  datalist.get(position).expertexp

        holder.expertrating.text = datalist.get(position).expertrating.toString()

        holder.noofrating.text = "(" + datalist.get(position).noofrating.toString() + ")"

        holder.sessiontime.text = "for " +  datalist.get(position).sessiontime


        val charge =  "₹" + datalist.get(position).expertcharge.toString()
        holder.expertcharge.text = charge


        val expertid = datalist.get(position).expertid

        holder.booknow.setOnClickListener {
            val intent = Intent(context, ExpertprofileActivity:: class.java)
            intent.putExtra("expertid", expertid)
            context.startActivity(intent)
        }

        val imagelink = datalist.get(position).expertpic
        Glide.with(context)
            .load(imagelink)
            .into(holder.expertpic)


    }


    class MyViewHolder(itemview : View ) : RecyclerView.ViewHolder(itemview){
        val expertname = itemview.findViewById<TextView>(R.id.tv_expertname)
        val expertmode = itemview.findViewById<TextView>(R.id.tv_expertmode)
        val expertlang = itemview.findViewById<TextView>(R.id.tv_expertlang)



        val expertdesign = itemview.findViewById<TextView>(R.id.tv_designation)
        val expertexp = itemview.findViewById<TextView>(R.id.tv_experince)
        val expertage = itemview.findViewById<TextView>(R.id.tv_age)

        val expertrating = itemview.findViewById<TextView>(R.id.tv_ratings)
        val noofrating = itemview.findViewById<TextView>(R.id.tv_noofrating)
        val expertcharge = itemview.findViewById<TextView>(R.id.tv_charge)
        val sessiontime = itemview.findViewById<TextView>(R.id.tv_sessiontime)
        val booknow = itemview.findViewById<Button>(R.id.btn_booknow)
        val expertpic = itemview.findViewById<ImageView>(R.id.iv_expertpic)
    }
}