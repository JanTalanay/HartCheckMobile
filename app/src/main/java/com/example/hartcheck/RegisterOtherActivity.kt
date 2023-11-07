package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.hartcheck.Model.MedicalCondition
import com.example.hartcheck.Model.MedicalHistory
import com.example.hartcheck.Model.Patients
import com.example.hartcheck.Model.PreviousMedication
import com.example.hartcheck.Remote.MedCondRemote.MedCondInstance
import com.example.hartcheck.Remote.PatientsRemote.PatientsInstance
import com.example.hartcheck.Remote.PreviousMedRemote.PreviousMedInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class RegisterOtherActivity : AppCompatActivity() {

    private lateinit var btn_current: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_other)
//        val patientID = intent.getIntExtra("patientID", 0)
        btn_current = findViewById(R.id.btn_current_cond)


        btn_current.setOnClickListener {
            insertMedicalCond()
        }




    }
    private fun insertMedicalCond(){
        val patientID = intent.getIntExtra("patientID", 0)
        val txtMedicalCon = findViewById<EditText>(R.id.txt_current_con)
        val txtConditionName = findViewById<EditText>(R.id.txt_condition_name)
        val MedicalCondservice = MedCondInstance.retrofitBuilder

        val MedicalCon = txtMedicalCon.text.toString()
        val ConditionName = txtConditionName.text.toString()

        val MedicalCondInfo = MedicalCondition(patientID = patientID, medicalCondition = MedicalCon, conditionName =ConditionName )

        MedicalCondservice.insertMedCond(MedicalCondInfo).enqueue(object : Callback<MedicalCondition> {
            override fun onResponse(call: Call<MedicalCondition>, response: Response<MedicalCondition>) {
                if(response.isSuccessful){
                    insertPrevMed()
                } else {
                    Log.d("MainActivity", "Failed to connect: " + response.code())

                }
            }
            override fun onFailure(call: Call<MedicalCondition>, t: Throwable) {
                Log.d ("MainActivity", "Failed to connect: : " + t.message)
                if (t is HttpException) {
                    val errorResponse = t.response()?.errorBody()?.string()
                    Log.d("MainActivity", "Error response: $errorResponse")
                }
            }
        })
    }
    private fun insertPrevMed(){
        val patientID = intent.getIntExtra("patientID", 0)
        val email = intent.getStringExtra("email")
        val otpHash = intent.getStringExtra("otpHash")

        val txt_Prev_Med = findViewById<EditText>(R.id.txt_Prev_Med)
        val PrevMedservice = PreviousMedInstance.retrofitBuilder

        val previousMed = txt_Prev_Med.text.toString()

        val PrevMedInfo = PreviousMedication(patientID = patientID, previousMed = previousMed)

        PrevMedservice.insertPrevMed(PrevMedInfo).enqueue(object : Callback<PreviousMedication> {
            override fun onResponse(call: Call<PreviousMedication>, response: Response<PreviousMedication>) {
                if(response.isSuccessful){

                    val intent = Intent(this@RegisterOtherActivity, RegisterOTPActivity::class.java)
                    intent.putExtra("email", email)
                    intent.putExtra("otpHash", otpHash)
                    startActivity(intent)
                } else {
                    Log.d("MainActivity", "Failed to connect: " + response.code())

                }
            }
            override fun onFailure(call: Call<PreviousMedication>, t: Throwable) {
                Log.d ("MainActivity", "Failed to connect: : " + t.message)
                if (t is HttpException) {
                    val errorResponse = t.response()?.errorBody()?.string()
                    Log.d("MainActivity", "Error response: $errorResponse")
                }
            }
        })
    }
}