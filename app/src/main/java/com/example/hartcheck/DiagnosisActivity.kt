package com.example.hartcheck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.hartcheck.Remote.ConditionsRemote.ConditionsInstance
import com.example.hartcheck.Remote.DiagnosisRemote.DiagnosisInstance
import com.example.hartcheck.Wrapper.ConditionsList
import com.example.hartcheck.Wrapper.DiagnosisList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiagnosisActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diagnosis)
        getDiagnosisList()
    }
    private fun getDiagnosisList(){
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
}