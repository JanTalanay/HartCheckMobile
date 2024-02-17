package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hartcheck.Adapter.ConsultationAdapter
import com.example.hartcheck.Data.ConsultationData

class AppointmentHistDetailActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var consultationAdapter: ConsultationAdapter
    private lateinit var consulList: MutableList<ConsultationData>
    private lateinit var btnbackapphist: Button
    private lateinit var txt_emp: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_hist_detail)

        val userID = intent.getIntExtra("userID", 0)
        val patientID = intent.getIntExtra("patientID", 0)
        val patientName = intent.getStringExtra("patientName")

//        //Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.fragment_appointment_hist_details, container, false)
        val frag = false

//        recyclerView = findViewById(R.id.consulList)
//        btnbackapphist = findViewById(R.id.btn_back_appoint_hist)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        consultationAdapter = ConsultationAdapter(consulList,frag, patientID,patientName = null, userID, AppointmentHistDetailsFragment::class.java)
//        recyclerView.adapter = consultationAdapter

//        enable this as default
//        txt_emp.visibility = GONE// you don't have any appointments today (put a null checker to show or not)
//        recyclerView.visibility = VISIBLE

        val history = listOf(
            ConsultationData("Condition           v","condition goes here"),//use get conditions here
            ConsultationData("Diagnosis           v","diagnosis goes here"),
            ConsultationData("Prescription        v","medicine goes here")

        )


//        consultationAdapter = ConsultationAdapter(history)
//        recyclerView.adapter = consultationAdapter


//        btnbackapphist.setOnClickListener {
//            val intent = Intent(this, HomeActivity::class.java)//change
//                intent.putExtra("patientID", patientID)
//                intent.putExtra("patientName", patientName)
//                intent.putExtra("userID", userID)
//            startActivity(intent)
//        }
    }
}