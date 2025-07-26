package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.model.AllChatsmodel
import com.example.myapplication.ui.PersonalchatActivity
import com.example.myapplication.R
import com.example.myapplication.model.PersonalchatsModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class AllchatsAdapter(val context: Context, private val rootView: View , var datalist: ArrayList<AllChatsmodel>) : RecyclerView.Adapter<AllchatsAdapter.MyViewHolder>() {


     val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users").child("1").child("expertschat")




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {


        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.rv_allchats, parent, false)
        return AllchatsAdapter.MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.expertChatname.text = datalist.get(position).expertname
       // holder.expertChatpic.setImageResource(datalist.get(position).expertpic)
        val imagelink = datalist.get(position).expertpic
        Glide.with(context)
            .load(imagelink)
            .into(holder.expertChatpic)

        lateinit var lastmsg : String
        lateinit var lasttime : String
         val exist : String = datalist.get(position).chatallowed







        holder.itemView.setOnClickListener {

            if(exist == "yes") {
                val intent = Intent(context, PersonalchatActivity::class.java)
                intent.putExtra("expertname", datalist[position].expertname)
                intent.putExtra("expertid", datalist[position].expertid)

                context.startActivity(intent)
            }else{

                 Snackbar.make(rootView, "Expert will be available after the session", Snackbar.LENGTH_LONG).show()

            }
        }



        val uid = "1"

        databaseReference.child(datalist.get(position).expertid.toString()).child("msgs").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var personalchatArraylist : ArrayList<PersonalchatsModel> = ArrayList<PersonalchatsModel>()

                personalchatArraylist.clear()
                if(snapshot.exists()){

                    for(datasnapshot in snapshot.children){

                        val data = datasnapshot.getValue(PersonalchatsModel:: class.java)
                        personalchatArraylist.add(data!!)
                    }

                    personalchatArraylist.sortBy { it.timestamp }

                    lastmsg = personalchatArraylist.get(personalchatArraylist.size - 1).sendmsg
                    lasttime = personalchatArraylist.get(personalchatArraylist.size - 1).sendtime

                    if(uid == personalchatArraylist.get(personalchatArraylist.size - 1).senderuid){
                        lastmsg = "You : " + lastmsg
                    }
                    holder.expertLastmsg.text = lastmsg
                    holder.time.text = lasttime
                    holder.time.visibility = View.VISIBLE


                }else{

                    holder.expertLastmsg.text = "Tap to chat"
                    holder.time.visibility = View.GONE


                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })








    }


    class MyViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview) {

        val expertChatname = itemview.findViewById<TextView>(R.id.tv_expertname)
        val expertLastmsg = itemview.findViewById<TextView>(R.id.tv_expertlastmsg)
        val expertChatpic = itemview.findViewById<ImageView>(R.id.iv_expertpic)
        val time = itemview.findViewById<TextView>(R.id.tv_time)
    }

}