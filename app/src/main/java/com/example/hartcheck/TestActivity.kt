package com.example.hartcheck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.hartcheck.Wrapper.Test
import com.example.hartcheck.Remote.PatientsDoctor.PatientsDoctorInstance
import com.example.hartcheck.Wrapper.PatientsDoctorAssign
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val testView: TextView = findViewById<TextView>(R.id.view_test)
        val editTest: EditText = findViewById<EditText>(R.id.edit_test)
        val editTest2: EditText = findViewById<EditText>(R.id.edit_test2)
        val editTest3: EditText = findViewById<EditText>(R.id.edit_test3)
        val editTest4: EditText = findViewById<EditText>(R.id.edit_test4)
        val editTest5: EditText = findViewById<EditText>(R.id.edit_test5)
        val editTest6: EditText = findViewById<EditText>(R.id.edit_test6)
        val editTest7: EditText = findViewById<EditText>(R.id.edit_test7)
        val testGet: Button = findViewById<Button>(R.id.btn_test)
        val testUpdate: Button = findViewById<Button>(R.id.btn_test2)
        val testDelete: Button = findViewById<Button>(R.id.btn_test3)
        val testInsert: Button = findViewById<Button>(R.id.btn_test4)

        val patientID = intent.getIntExtra("patientID", 0)
//        val userID = intent.getIntExtra("userID", 0)
//        testView.text = userID.toString()

//        Log.d("TestActivity", "User ID: $userID")

        testGet.setOnClickListener {
//            getPatientID()
            getDoctorID()
        }

    }
    private fun getDoctorID(){
        val patientID = intent.getIntExtra("patientID", 0)
        val service = PatientsDoctorInstance.retrofitBuilder

        service.getHealthCareProfessionals(patientID).enqueue(object : Callback<PatientsDoctorAssign> {
            override fun onResponse(call: Call<PatientsDoctorAssign>, response: Response<PatientsDoctorAssign>) {
                if (response.isSuccessful) {
                    val healthCareProfessionals = response.body()
                    if (healthCareProfessionals != null) {
                        for (professional in healthCareProfessionals.HealthCareName) {
                            Log.d("TestActivity", "First Name: ${professional.firstName}, Last Name: ${professional.lastName}")
                        }
                    }
                } else {
                    Log.d("TestActivity", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<PatientsDoctorAssign>, t: Throwable) {
                Log.d("TestActivity", "Failure: ${t.message}")
            }
        })
    }
}