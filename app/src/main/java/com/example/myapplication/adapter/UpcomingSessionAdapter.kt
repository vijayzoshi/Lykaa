package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.model.UpcomingSessionModel
import com.example.myapplication.R
import com.example.myapplication.model.SessionData
import com.example.myapplication.ui.CancelConfirmationActivity
import com.example.myapplication.ui.SessionDetailsActivity
import com.example.myapplication.ui.SessionidActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue


class UpcomingSessionAdapter( val viewtype : String, val context: Context, var datalist: ArrayList<UpcomingSessionModel>) : RecyclerView.Adapter<UpcomingSessionAdapter.MyViewHolder>() {



    lateinit var databaseRefrence : DatabaseReference
    lateinit var databaseRefrenceexpert : DatabaseReference

    lateinit var snackbar: Snackbar
    private lateinit var sharedPref : SharedPreferences




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {


        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.rv_upcomingsession, parent, false)
        return UpcomingSessionAdapter.MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        sharedPref = context.getSharedPreferences("userdetails", Context.MODE_PRIVATE)
        val uid = sharedPref.getString("userid", "haha")
        databaseRefrence = FirebaseDatabase.getInstance().getReference()
        databaseRefrenceexpert = FirebaseDatabase.getInstance().getReference()




        val expertid = datalist.get(position).expertid

        databaseRefrenceexpert.child("experts").child(expertid.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                holder.expertName.text =snapshot.child("expertname").getValue<String>()
                holder.expertDesign.text =snapshot.child("expertdesign").getValue<String>()
               //  holder.expertPic.setImageResource(datalist.get(position).experpic)

                val imagelink = snapshot.child("expertpic").getValue<String>()
                Glide.with(context)
                    .load(imagelink)
                    .into(holder.expertPic)


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        )


        holder.sessionDate.text = datalist.get(position).sessiondate
        holder.sessionTime.text = datalist.get(position).sessiontime.take(8)
        holder.sessionMode.text = datalist.get(position).sessionmode




        val ss = datalist.get(position).sessionid.toString()


        if(viewtype=="upcoming"){

            holder.cancelledTv.visibility = View.GONE
            holder.completedTv.visibility = View.GONE



        }

        if(viewtype=="previous"){
            holder.cancelBtn.visibility = View.GONE
        }



        if(datalist.get(position).sessionstatus == "Cancelled"){
            holder.completedTv.visibility = View.GONE
        }else{
            holder.cancelledTv.visibility = View.GONE

        }


        holder.cancelBtn.setOnClickListener {

            MaterialAlertDialogBuilder(context)
                .setTitle(context.getString(R.string.cancel_this_session))
                .setNegativeButton("No") { dialog, which ->
                    dialog.cancel()
                }
                .setPositiveButton("Yes") { dialog, which ->

                    databaseRefrence.child("users").child(uid.toString()).child("sessions").child("upcomingsesions").child(ss).removeValue()
                    notifyItemChanged(position)


                    val cancelledsessiondata = SessionData("Cancelled",datalist.get(position).timestamp,expertid, datalist.get(position).sessiondate,datalist.get(position).sessiontime,  datalist.get(position).sessionmode, datalist.get(position).sessionid)
                    databaseRefrence.child("users").child(uid.toString()).child("sessions").child("previoussessions").child(datalist.get(position).sessionid.toString()).setValue(cancelledsessiondata)




                    val intent = Intent(context, CancelConfirmationActivity::class.java)
                    context.startActivity(intent)

                    /*
                    snackbar =  Snackbar.make(it, "Session Deleted", Snackbar.LENGTH_LONG)
                    snackbar.setAction("Ok") {

                            // Responds to click on the action
                            snackbar.dismiss()

                        }
                        .show()
*/

                }
                .show()


        }

        holder.viewDetails.setOnClickListener {
            val intent = Intent(context, SessionidActivity:: class.java)
            intent.putExtra("sessionid", datalist.get(position).sessionid)
            intent.putExtra("expertid", datalist.get(position).expertid)
            intent.putExtra("sessionstatus", datalist.get(position).sessionstatus)


            context.startActivity(intent)
        }







    }


    class MyViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview) {

        val expertName = itemview.findViewById<TextView>(R.id.tv_expertname)
        val expertPic = itemview.findViewById<ImageView>(R.id.iv_expertpic)


        val expertDesign = itemview.findViewById<TextView>(R.id.tv_expertdesign)
        val sessionDate = itemview.findViewById<TextView>(R.id.tv_sessiondate)
        val sessionTime = itemview.findViewById<TextView>(R.id.tv_sessiontime)
        val sessionMode = itemview.findViewById<TextView>(R.id.tv_sessionmode)



        val cancelBtn = itemview.findViewById<Button>(R.id.btn_cancel)
        val cancelledTv = itemview.findViewById<TextView>(R.id.tv_cancelled)
        val completedTv = itemview.findViewById<TextView>(R.id.tv_completed)


        val viewDetails = itemview.findViewById<Button>(R.id.btn_viewdetails)


    }

}