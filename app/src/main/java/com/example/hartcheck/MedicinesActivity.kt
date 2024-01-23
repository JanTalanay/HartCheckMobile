package com.example.hartcheck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.hartcheck.Remote.ConditionsRemote.ConditionsInstance
import com.example.hartcheck.Remote.MedicineRemote.MedicineInstance
import com.example.hartcheck.Wrapper.ConditionsList
import com.example.hartcheck.Wrapper.MedicineList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MedicinesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicines)
        getMedicinesList()
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
}