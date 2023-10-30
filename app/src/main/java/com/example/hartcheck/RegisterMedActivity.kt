package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.hartcheck.Model.BMIType
import com.example.hartcheck.Model.BodyMass
import com.example.hartcheck.Model.BugReport
import com.example.hartcheck.Model.MedicalHistory
import com.example.hartcheck.Remote.BodyMassRemote.BodyMassInstance
import com.example.hartcheck.Remote.BugReportRemote.BugReportInstance
import com.example.hartcheck.Remote.MedHisRemote.MedHisInstance
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterMedActivity : AppCompatActivity() {
    private lateinit var gsc: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registermed)
        GoogleRegisterFinal()

//        val patientID = intent.getIntExtra("patientID", 0)
//        Toast.makeText(this, "Registration Successful ${patientID}", Toast.LENGTH_SHORT).show()

        val btn_medHis_bodyMass = findViewById<Button>(R.id.btn_medHis_bodyMass)
        btn_medHis_bodyMass.setOnClickListener {
//            val intent = Intent(this, LoginMain::class.java)
//            startActivity(intent)
            insertMedHis()
            insertBodyMass()
        }


    }

    private fun GoogleRegisterFinal() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(this, gso)
    }

    private fun goSignOut() {
        gsc.signOut().addOnSuccessListener {
            startActivity(Intent(this, LoginMain::class.java))
            finish()
        }
    }

    private fun insertMedHis() {//to be fix
        val patientID = intent.getIntExtra("patientID", 0)

        val txtMedCond: EditText = findViewById(R.id.txt_medcond)
        val txtpastSurge: EditText = findViewById(R.id.txt_pastSurg)

        val medCond = txtMedCond.text.toString()
        val pastSurge = txtpastSurge.text.toString()

        val medHisInfo = MedicalHistory(patientID = patientID, medicalHistory = medCond, pastSurgicalHistory = pastSurge)

        val medHisService = MedHisInstance.retrofitBuilder
        medHisService.insertMedHis(medHisInfo).enqueue(object : Callback<MedicalHistory> {
            override fun onResponse(call: Call<MedicalHistory>, response: Response<MedicalHistory>) {
                if (response.isSuccessful) {
                    // Successfully deleted the bug report

                } else {
                    // Handle the error response
                }
            }

            override fun onFailure(call: Call<MedicalHistory>, t: Throwable) {
                // Handle network or other exceptions
            }
        })
    }
    private fun insertBodyMass() {
        val patientID = intent.getIntExtra("patientID", 0)

        val txtBodyMassHeight: EditText = findViewById(R.id.input_height)
        val txtBodyMassWeight: EditText = findViewById(R.id.input_weight)


        val bodyMassHeight = txtBodyMassHeight.text.toString()
        val bodyMassWeight = txtBodyMassWeight.text.toString()
//        val BMIType = 0

        val heightInMeters = bodyMassHeight.toDouble() / 100
        val weightInKg = bodyMassWeight.toDouble()
        val bmi = weightInKg / (heightInMeters * heightInMeters)

        val BMIType = when {//setting of number needs to be fx
            bmi < 18.5 -> 1
            bmi >= 18.5 && bmi < 25 -> 2
            bmi >= 25 && bmi < 30 -> 3
            bmi >= 30 && bmi < 35 -> 4
            bmi >= 35 && bmi < 40 -> 5
            else -> 1
        }

        val bodyMassInfo = BodyMass(patientID = patientID, BMITypeID = BMIType, weight = bodyMassWeight.toInt(), height = bodyMassHeight.toInt() )

        val bodyMassService = BodyMassInstance.retrofitBuilder
        bodyMassService.insertBodyMass(bodyMassInfo).enqueue(object : Callback<BodyMass> {
            override fun onResponse(call: Call<BodyMass>, response: Response<BodyMass>) {
                if (response.isSuccessful) {
                    // Successfully deleted the bug report
                    goSignOut()

//                    val intent = Intent(this@RegisterMedActivity, LoginMain::class.java)
//                    startActivity(intent)
                } else {
                    // Handle the error response
                }
            }

            override fun onFailure(call: Call<BodyMass>, t: Throwable) {
                // Handle network or other exceptions
            }
        })
    }
}