package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hartcheck.Adapter.ConsultationAdapter

class AppointmentHistDetails : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var consultationAdapter: ConsultationAdapter
    //private lateinit var consulList: MutableList<ConsultationData>
    private lateinit var btnbackapphist: Button
    private lateinit var txt_emp: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_appointment_hist_details)


//        val patientID = arguments?.getInt(AppointmentHistDetailsFragment.ARG_PATIENT_ID)
//        val userID = arguments?.getInt(AppointmentHistDetailsFragment.ARG_USER_ID)
//        val patientName = arguments?.getString(AppointmentHistDetailsFragment.ARG_PATIENT_NAME)
//        // Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.fragment_appointment_hist_details, container, false)
//        val frag = true

//        recyclerView = view.findViewById(R.id.consulList)
//        btnbackapphist = view.findViewById(R.id.btn_back_appoint_hist)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        consultationAdapter = ConsultationAdapter(consulList,frag, patientID,patientName = null, userID, AppointmentHistDetailsFragment::class.java)
//        recyclerView.adapter = consultationAdapter

            //enable this as default
//        txt_emp.visibility = View.GONE// you don't have any appointments today (put a null checker to show or not)
//        recyclerView.visibility = View.VISIBLE

//        val history = listOf(
//            ConsultationData("Condition           v","condition goes here"),//use get conditions here
//            ConsultationData("Diagnosis           v","diagnosis goes here"),
//            ConsultationData("Prescription        v","medicine goes here")
//
//        )


//        consultationAdapter = ConsultationAdapter(history)
//        recyclerView.adapter = consultationAdapter


            btnbackapphist.setOnClickListener {
                val intent = Intent(this, HomeActivity::class.java)//change
                intent.putExtra("patientID", patientID)
                intent.putExtra("patientName", patientName)
                intent.putExtra("userID", userID)
                startActivity(intent)
            }
    }
}