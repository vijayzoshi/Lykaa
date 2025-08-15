package com.example.myapplication.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID

class BookingActivity : AppCompatActivity() {

    private lateinit var sessionModeGroup: RadioGroup
    private lateinit var dateContainer: LinearLayout
    private lateinit var timeSlotContainer: GridLayout
    private lateinit var etPatientName: EditText
    private lateinit var btnNext: Button

    private var selectedDate: String? = null
    private var selectedTime: String? = null
    private var selectedSession: String? = null

    private val dbRef = FirebaseDatabase.getInstance().getReference("doctorAvailability")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session_pass)

        sessionModeGroup = findViewById(R.id.sessionModeGroup)
        dateContainer = findViewById(R.id.dateContainer)
        timeSlotContainer = findViewById(R.id.timeSlotContainer)
        etPatientName = findViewById(R.id.etPatientName)
        btnNext = findViewById(R.id.btnNext)

        loadAvailableDates()

        btnNext.setOnClickListener {
            bookAppointment()
        }
    }

    private fun loadAvailableDates() {
        dbRef.get().addOnSuccessListener { snapshot ->
            snapshot.children.forEach { dateSnap ->
                val date = dateSnap.key ?: return@forEach
                val btn = Button(this).apply {
                    text = date
                    setOnClickListener {
                        selectedDate = date
                        highlightSelectedButton(this, dateContainer)
                        loadTimeSlots(date)
                    }
                }
                dateContainer.addView(btn)
            }
        }
    }

    private fun loadTimeSlots(date: String) {
        timeSlotContainer.removeAllViews()
        dbRef.child(date).get().addOnSuccessListener { snapshot ->
            snapshot.children.forEach { timeSnap ->
                val time = timeSnap.getValue(String::class.java) ?: return@forEach
                val btn = Button(this).apply {
                    text = time
                    setOnClickListener {
                        selectedTime = time
                        highlightSelectedButton(this, timeSlotContainer)
                    }
                }
                timeSlotContainer.addView(btn)
            }
        }
    }

    private fun bookAppointment() {
        selectedSession = when (sessionModeGroup.checkedRadioButtonId) {
            R.id.rbVirtual -> "Virtual"
            R.id.rbInPerson -> "InPerson"
            else -> null
        }

        val name = etPatientName.text.toString().trim()

        if (selectedSession == null || selectedDate == null || selectedTime == null || name.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val appointmentId = UUID.randomUUID().toString()
        val appointment = mapOf(
            "sessionMode" to selectedSession,
            "date" to selectedDate,
            "time" to selectedTime,
            "patientName" to name
        )

        FirebaseDatabase.getInstance().getReference("appointments")
            .child(appointmentId)
            .setValue(appointment)
            .addOnSuccessListener {
                Toast.makeText(this, "Appointment booked", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Booking failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun highlightSelectedButton(selectedBtn: View, container: ViewGroup) {
        for (i in 0 until container.childCount) {
            val child = container.getChildAt(i)
            child.setBackgroundColor(Color.WHITE)
        }
       // selectedBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.daz))
    }
}
