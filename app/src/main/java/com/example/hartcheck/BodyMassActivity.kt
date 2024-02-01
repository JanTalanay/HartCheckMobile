package com.example.hartcheck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class BodyMassActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_body_mass)

        val calculate_btn = findViewById<Button>(R.id.calculate_btn)
        val etHeight = findViewById<EditText>(R.id.etHeight)
        val etWeight = findViewById<EditText>(R.id.etWeight)
        val bmi_tv = findViewById<TextView>(R.id.bmi_tv)
        val status = findViewById<TextView>(R.id.status)


        calculate_btn.setOnClickListener {
            // Check if the height EditText and Weight EditText are not empty
            if (etHeight.text.isNotEmpty() && etWeight.text.isNotEmpty()) {
                val height = etHeight.text.toString().toFloat()
                val weight = etWeight.text.toString().toFloat()
                // calculateBMI will return BMI
                val BMI = calculateBMI(height, weight)
                bmi_tv.text = "Your BMI is: $BMI"
                // update the status text as per the bmi conditions
                status.text = when {
                    BMI < 18.5 -> "Under Weight"
                    BMI >= 18.5 && BMI < 24.9 -> "Normal"
                    BMI >= 24.9 && BMI < 30 -> "Overweight"
                    else -> "Obese"
                }
            } else {
                Toast.makeText(this, "please enter the valid height and weight", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun calculateBMI(height: Float, weight: Float): Float {
        val heightInMeter = height.toFloat() / 100
        return weight.toFloat() / (heightInMeter * heightInMeter)
    }
}