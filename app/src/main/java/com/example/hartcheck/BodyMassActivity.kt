package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.hartcheck.Model.BodyMass
import com.example.hartcheck.Remote.BodyMassRemote.BodyMassInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BodyMassActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_body_mass)

        val calculate_btn = findViewById<Button>(R.id.calculate_btn)
        val etHeight = findViewById<EditText>(R.id.etHeight)
        val etWeight = findViewById<EditText>(R.id.etWeight)
//        val bmi_tv = findViewById<TextView>(R.id.bmi_tv)
//        val status = findViewById<TextView>(R.id.status)


        calculate_btn.setOnClickListener {
            // Check if the height EditText and Weight EditText are not empty
            if (etHeight.text.isNotEmpty() && etWeight.text.isNotEmpty()) {
                val height = etHeight.text.toString().toFloat()
                val weight = etWeight.text.toString().toFloat()
                // calculateBMI will return BMI and BMIType
                val (BMI, BMIType) = calculateBMI(height, weight)
                insertBodyMass(BMI, BMIType, weight, height)
            } else {
                Toast.makeText(this, "please enter the valid height and weight", Toast.LENGTH_SHORT).show()
            }
        }


    }

//    private fun calculateBMI(height: Float, weight: Float): Float {
//        val heightInMeter = height.toFloat() / 100
//        return weight.toFloat() / (heightInMeter * heightInMeter)
//    }
private fun calculateBMI(height: Float, weight: Float): Pair<Float, Int> {
    val heightInMeter = height.toFloat() / 100
    val BMI = weight.toFloat() / (heightInMeter * heightInMeter)

    val BMIType = when {
        BMI < 18.5 -> 1
        BMI >= 18.5 && BMI < 24.9 -> 2
        BMI >= 24.9 && BMI < 30 -> 3
        BMI >= 30 && BMI < 35 -> 4
        BMI >= 35 && BMI < 40 -> 5
        else -> 1
    }

    return Pair(BMI, BMIType)
}


    private fun insertBodyMass(BMI: Float, BMIType: Int, weight: Float, height: Float) {
        val patientID = intent.getIntExtra("patientID", 0)
        val email = intent.getStringExtra("email")
        val otpHash = intent.getStringExtra("otpHash")

        val bodyMassInfo = BodyMass(patientID = patientID, BMITypeID = BMIType, weight = weight.toInt(), height = height.toInt() ) // Assuming a standard height for simplicity

        val bodyMassService = BodyMassInstance.retrofitBuilder
        bodyMassService.insertBodyMass(bodyMassInfo).enqueue(object : Callback<BodyMass> {
            override fun onResponse(call: Call<BodyMass>, response: Response<BodyMass>) {
                if (response.isSuccessful) {
                    // Handle successful response
//                    Toast.makeText(this@BodyMassActivity, "Added Body Mass! $otpHash", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@BodyMassActivity, RegisterOTPActivity::class.java)
                    intent.putExtra("email", email)
                    intent.putExtra("otpHash", otpHash)
                    intent.putExtra("patientID", patientID)
                    startActivity(intent)
                } else {
                    // Handle error response
                }
            }

            override fun onFailure(call: Call<BodyMass>, t: Throwable) {
                // Handle network or other exceptions
            }
        })
    }

}