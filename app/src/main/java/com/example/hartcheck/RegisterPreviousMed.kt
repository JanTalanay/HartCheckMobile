package com.example.hartcheck

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.example.hartcheck.Model.MedicalCondition
import com.example.hartcheck.Model.MedicalHistory
import com.example.hartcheck.Model.Patients
import com.example.hartcheck.Remote.MedCondRemote.MedCondInstance
import com.example.hartcheck.Remote.MedHisRemote.MedHisInstance
import com.example.hartcheck.Remote.PatientsRemote.PatientsInstance
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.min

class RegisterPreviousMed : AppCompatActivity() {

    private val calendar = Calendar.getInstance()
    private var patientID: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_previous_med)
        val inputFields = findViewById<LinearLayout>(R.id.inputFields)
        val inputFieldsDate = findViewById<LinearLayout>(R.id.inputFieldsDate)
//        val btnmedHisbodyMass = findViewById<Button>(R.id.btn_prevMedication_next)

//        val patientID = intent.getIntExtra("patientID", 0)
//        Toast.makeText(this@RegisterPreviousMed, "$patientID", Toast.LENGTH_SHORT).show()

        patientID = getPatientID()
        Toast.makeText(this@RegisterPreviousMed, "$patientID", Toast.LENGTH_SHORT).show()


        val addButton = findViewById<Button>(R.id.btn_addMed)
        addButton.setOnClickListener {
            if (inputFields.childCount < 5 && inputFieldsDate.childCount <5){
                showModal()
            }
        }

        val removeButton = findViewById<Button>(R.id.btn_removeMed)
        removeButton.setOnClickListener {
            if (inputFields.childCount > 1 && inputFieldsDate.childCount > 1) {
                inputFields.removeViewAt(inputFields.childCount - 1)
                inputFieldsDate.removeViewAt(inputFieldsDate.childCount - 1)
            }
        }

        //this is where you get values
        val sendBtn = findViewById<Button>(R.id.btn_prevMedication_next)
        sendBtn.setOnClickListener {
            val medCondInfos = mutableListOf<MedicalCondition>()
            for (i in 0 until min(inputFields.childCount, inputFieldsDate.childCount)) {
                val view: View = inputFields.getChildAt(i)
                val viewDate: View = inputFieldsDate.getChildAt(i)
                if (view is EditText && viewDate is TextView) {
                    val medCond = view.text.toString()
                    val date = viewDate.text.toString()
                    val medCondInfo = MedicalCondition(patientID = patientID, medicalCondition = medCond, date = date)
                    medCondInfos.add(medCondInfo)
                }
            }
            insertMultipleMedCond(medCondInfos)
        }



    }
    private fun showModal(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.popup_register_prev_med)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btn_close:Button = dialog.findViewById(R.id.btn_modal_deny_med)
        val btn_confirm = dialog.findViewById<Button>(R.id.btn_modal_confirm_med)
        val input_med = dialog.findViewById<EditText>(R.id.edit_prev_med)
        val input_date = dialog.findViewById<EditText>(R.id.edit_prev_med_date)

        input_date.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this, {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, monthOfYear, dayOfMonth)
                    val dateFormat = SimpleDateFormat("yyyy-dd-MM", Locale.getDefault())
                    val formattedDate = dateFormat.format(selectedDate.time)
                    input_date.setText("$formattedDate")
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            // Show the DatePicker dialog
            datePickerDialog.show()
        }
        btn_confirm.setOnClickListener {
            val inputFields = findViewById<LinearLayout>(R.id.inputFields)
            val inputFieldsDate = findViewById<LinearLayout>(R.id.inputFieldsDate)
            val inputField = EditText(this)
            val inputFieldDate = EditText(this)
            inputField.text = input_med.text
            inputFieldDate.text = input_date.text

            inputField.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
            }
            inputFieldDate.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
            }
            inputField.setPadding(41,25,25,25)
            inputFieldDate.setPadding(41,25,25,25)
            inputField.textSize = 18F
            inputFieldDate.textSize = 18F


            inputFields.addView(inputField)
            inputFieldsDate.addView(inputFieldDate)
            dialog.dismiss()
        }

        btn_close.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
    private fun insertMultipleMedCond(medConInfo: List<MedicalCondition>) {

        val email = intent.getStringExtra("email")
        val otpHash = intent.getStringExtra("otpHash")


        val medCondService = MedCondInstance.retrofitBuilder
        medConInfo.forEach { medConInfo ->
            medCondService.insertMedCond(medConInfo).enqueue(object : Callback<MedicalCondition> {
                override fun onResponse(call: Call<MedicalCondition>, response: Response<MedicalCondition>) {
                    if (response.isSuccessful) {
                        // Successfully inserted the medical history
//                        Toast.makeText(this@RegisterPreviousMed, "Added History of Previous Condition", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegisterPreviousMed, RegisterSurgHistory::class.java)
                        intent.putExtra("email", email)
                        intent.putExtra("otpHash", otpHash)
                        intent.putExtra("patientID", patientID)
                        startActivity(intent)

                    } else {
                        // Handle the error response
                        val errorBody = response.errorBody()?.string()
                        Log.d("MainActivity", "Failed to connect: " + response.code() + ", error: " + errorBody)
                    }
                }

                override fun onFailure(call: Call<MedicalCondition>, t: Throwable) {
                    // Handle network or other exceptions
                    Log.d("MainActivity", "Failed to connect: : " + t.message)
                }
            })
        }
    }
    private fun getPatientID(): Int {
        val userID = intent.getIntExtra("userID", 0)
        val service = PatientsInstance.retrofitBuilder

        service.getPatientsID(userID).enqueue(object : Callback<Patients> {
            override fun onResponse(call: Call<Patients>, response: Response<Patients>) {
                if(response.isSuccessful){
                    response.body()?.let { patients ->
                        if(userID == patients.usersID){
                            patientID = patients.patientID!!
                        } else {
                            Log.d("MainActivity", "Wrong: " + response.code())
                        }
                    }
                } else {
                    Log.d("MainActivity", "Failed to connect: " + response.code())
                }
            }
            override fun onFailure(call: Call<Patients>, t: Throwable) {
                Log.d ("MainActivity", "Failed to connect: : " + t.message)
                if (t is HttpException) {
                    val errorResponse = t.response()?.errorBody()?.string()
                    Log.d("MainActivity", "Error response: $errorResponse")
                }
            }
        })
        return patientID
    }



}