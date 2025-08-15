/*package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.EventsModel
import com.example.myapplication.ui.EventDetailsActivity
import com.google.android.material.button.MaterialButton


class EventAdapter(val context: Context, var dataList: ArrayList<EventsModel>) : RecyclerView.Adapter<EventAdapter.MyViewHolder>() {


    // var  databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("users").child("1")


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_events, parent, false)
        return MyViewHolder(view)
    }

    /*  fun filterDataList(filterList: ArrayList<OrderModal>) {
          dataList = filterList
          notifyDataSetChanged()
      }


     */

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = dataList.get(position)

        holder.eventnameTV.text = data.eventname
        holder.eventtimeTV.text = data.eventtime
        holder.eventlocationIV.text = data.eventlocation
        holder.eventchargeIV.text = data.eventcharge.toString()
       // holder.eventpicIV.text = data.eventpic




        holder.joinIV.setOnClickListener {
            val intent = Intent(context, EventDetailsActivity:: class.java)
            intent.putExtra("eventid", data.eventid)
            context.startActivity(intent)
        }



    }

    override fun getItemCount() = dataList.size


    class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val eventnameTV = itemview.findViewById<TextView>(R.id.tv_eventname)
        val eventtimeTV = itemview.findViewById<TextView>(R.id.tv_eventtime)
        val eventlocationIV = itemview.findViewById<TextView>(R.id.tv_eventlocation)
        val eventchargeIV = itemview.findViewById<TextView>(R.id.tv_eventcharge)
        val eventpicIV = itemview.findViewById<ImageView>(R.id.iv_eventpic)



        val joinIV = itemview.findViewById<MaterialButton>(R.id.btn_join)




    }




}

 */