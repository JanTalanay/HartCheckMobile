package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hartcheck.Adapter.ConsultationAdapter
import com.example.hartcheck.Data.ConsultationData
import com.example.hartcheck.Remote.ConditionsRemote.ConditionsInstance
import com.example.hartcheck.Remote.DiagnosisRemote.DiagnosisInstance
import com.example.hartcheck.Remote.MedicineRemote.MedicineInstance
import com.example.hartcheck.Wrapper.ConditionsList
import com.example.hartcheck.Wrapper.DiagnosisList
import com.example.hartcheck.Wrapper.MedicineList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
//        val frag = false

        recyclerView = findViewById(R.id.recyclerView_hist)
        btnbackapphist = findViewById(R.id.btn_back_appoint_hist)
        recyclerView.layoutManager = LinearLayoutManager(this)
//        consultationAdapter = ConsultationAdapter(consulList)
//        recyclerView.adapter = consultationAdapter

//        enable this as default
//        txt_emp.visibility = GONE// you don't have any appointments today (put a null checker to show or not)
        recyclerView.visibility = VISIBLE

        val history = listOf(
            ConsultationData("Condition           v","condition goes here"),//use get conditions here
            ConsultationData("Diagnosis           v","diagnosis goes here"),
            ConsultationData("Prescription        v","medicine goes here")

        )


        consultationAdapter = ConsultationAdapter(history)
        recyclerView.adapter = consultationAdapter


//        btnbackapphist.setOnClickListener {
//            val intent = Intent(this, HomeActivity::class.java)//change
//                intent.putExtra("patientID", patientID)
//                intent.putExtra("patientName", patientName)
//                intent.putExtra("userID", userID)
//            startActivity(intent)
//        }
    }
    private fun getDiagnosisList(){//SORT THIS BASED ON CONSULTATION ID( REMOVE CONDITIONS, MEDICINE, AND DIAGNOSIS ACT)
        val patientID = intent.getIntExtra("patientID", 0)
        val service = DiagnosisInstance.retrofitBuilder
        val testView: TextView = findViewById(R.id.txt_consultation_patientdiagnos)

        service.getDiagnosisByPatientID(patientID).enqueue(object : Callback<DiagnosisList> {
            override fun onResponse(call: Call<DiagnosisList>, response: Response<DiagnosisList>) {
                if (response.isSuccessful) {
                    val diagnosis = response.body()
                    if (diagnosis != null) {
                        for (professional in diagnosis.DiagnosisInfo) {
                            Log.d("TestActivity", "${professional.diagnosis}")
                            testView.text = "${professional.diagnosis}"
                        }
                    }
                } else {
                    Log.d("TestActivity", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DiagnosisList>, t: Throwable) {
                Log.d("TestActivity", "Failure: ${t.message}")
            }
        })
    }
    private fun getMedicinesList(){
        val patientID = intent.getIntExtra("patientID", 0)
        val service = MedicineInstance.retrofitBuilder
        val testView: TextView = findViewById(R.id.txt_consultation_patientmedicine)

        service.getMedicinesByPatientID(patientID).enqueue(object : Callback<MedicineList> {
            override fun onResponse(call: Call<MedicineList>, response: Response<MedicineList>) {
                if (response.isSuccessful) {
                    val medicine = response.body()
                    if (medicine != null) {
                        for (professional in medicine.MedicineInfo) {
                            Log.d("TestActivity", "${professional.medicine}}")
                            testView.text = "Patient condition: ${professional.medicine}"
                        }
                    }
                } else {
                    Log.d("TestActivity", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MedicineList>, t: Throwable) {
                Log.d("TestActivity", "Failure: ${t.message}")
            }
        })
    }
    private fun getConsultationsList(){
        val patientID = intent.getIntExtra("patientID", 0)
        val service = ConditionsInstance.retrofitBuilder
        val testView: TextView = findViewById(R.id.txt_consultation_patientCond)

        service.getConditionByPatientID(patientID).enqueue(object : Callback<ConditionsList> {
            override fun onResponse(call: Call<ConditionsList>, response: Response<ConditionsList>) {
                if (response.isSuccessful) {
                    val conditions = response.body()
                    if (conditions != null) {
                        for (professional in conditions.ConditionsInfo) {
                            Log.d("TestActivity", "DoctorIDs: ${professional.condition}")
                            testView.text = "Patient condition: ${professional.condition}"
                        }
                    }
                } else {
                    Log.d("TestActivity", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ConditionsList>, t: Throwable) {
                Log.d("TestActivity", "Failure: ${t.message}")
            }
        })
    }
}