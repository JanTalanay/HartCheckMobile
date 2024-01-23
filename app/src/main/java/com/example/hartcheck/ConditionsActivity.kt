package com.example.hartcheck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.hartcheck.Remote.ConditionsRemote.ConditionsInstance
import com.example.hartcheck.Remote.PatientsDoctorRemote.PatientsDoctorInstance
import com.example.hartcheck.Wrapper.ConditionsList
import com.example.hartcheck.Wrapper.PatientsDoctorList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConditionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conditions)
        getConsultationsList()
//        val patientID = intent.getIntExtra("patientID", 0)
//        Toast.makeText(this@ConditionsActivity, "$patientID", Toast.LENGTH_SHORT).show()

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