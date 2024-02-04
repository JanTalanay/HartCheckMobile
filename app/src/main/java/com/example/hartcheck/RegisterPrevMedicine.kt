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
import com.example.hartcheck.Model.PreviousMedication
import com.example.hartcheck.Remote.PreviousMedRemote.PreviousMedInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.min

class RegisterPrevMedicine : AppCompatActivity() {

    private val calendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_prevmedicine)
        val inputFields = findViewById<LinearLayout>(R.id.inputFields2)
        val inputFieldsDate = findViewById<LinearLayout>(R.id.inputFieldsDate2)
        val inputFieldsDosage = findViewById<LinearLayout>(R.id.inputFieldsDosage)

        val addButton = findViewById<Button>(R.id.btn_addMedicine)
        addButton.setOnClickListener {
            if (inputFields.childCount < 5 && inputFieldsDate.childCount <5 && inputFieldsDosage.childCount <5){
                showModal()
            }

        }

        val removeButton = findViewById<Button>(R.id.btn_removeMedicine)
        removeButton.setOnClickListener {
            if (inputFields.childCount > 1 && inputFieldsDate.childCount > 1 && inputFieldsDosage.childCount > 1) {
                inputFields.removeViewAt(inputFields.childCount - 1)
                inputFieldsDate.removeViewAt(inputFieldsDate.childCount - 1)
                inputFieldsDosage.removeViewAt(inputFieldsDosage.childCount - 1)
            }
        }
        val patientID = intent.getIntExtra("patientID", 0)

        //this is where you get values
        val sendBtn = findViewById<Button>(R.id.btn_medHis_medicine)
        sendBtn.setOnClickListener {
            val prevMedInfos = mutableListOf<PreviousMedication>()
            for (i in 0 until min(inputFields.childCount, inputFieldsDate.childCount)) {
                val view: View = inputFields.getChildAt(i)
                val viewDosage: View = inputFieldsDosage.getChildAt(i)
                val viewDate: View = inputFieldsDate.getChildAt(i)
                if (view is EditText && viewDate is TextView && viewDosage is EditText) {
                    val prevMed = view.text.toString()
                    val dosage = viewDosage.text.toString()
                    val date = viewDate.text.toString()
                    val prevMedInfo = PreviousMedication(patientID = patientID, previousMed = prevMed, dosage = dosage.toFloat(), date = date)
                    prevMedInfos.add(prevMedInfo)
                }
            }
            insertMultiplePrevMed(prevMedInfos)
        }

    }
    private fun showModal(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.popup_register_prevmedicine)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btn_close:Button = dialog.findViewById(R.id.btn_modal_deny_medicine)
        val btn_confirm = dialog.findViewById<Button>(R.id.btn_modal_confirm_medicine)
        val input_med = dialog.findViewById<EditText>(R.id.edit_prev_medicine)
        val input_dosage = dialog.findViewById<EditText>(R.id.edit_prev_medicine_dosage)
        val input_date = dialog.findViewById<EditText>(R.id.edit_prev_medicine_date)

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
            val inputFields = findViewById<LinearLayout>(R.id.inputFields2)
            val inputFieldsDate = findViewById<LinearLayout>(R.id.inputFieldsDate2)
            val inputFieldsDosage = findViewById<LinearLayout>(R.id.inputFieldsDosage)

            val inputField = EditText(this)
            val inputFieldDosage = EditText(this)
            val inputFieldDate = EditText(this)
            inputField.text = input_med.text
            inputFieldDate.text = input_date.text
            inputFieldDosage.text = input_dosage.text

            inputField.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
            }
            inputFieldDosage.layoutParams = LinearLayout.LayoutParams(
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
            inputFieldDosage.setPadding(41,25,25,25)
            inputField.textSize = 18F
            inputFieldDate.textSize = 18F
            inputFieldDosage.textSize = 18F


            inputFields.addView(inputField)
            inputFieldsDate.addView(inputFieldDate)
            inputFieldsDosage.addView(inputFieldDosage)
            dialog.dismiss()
        }

        btn_close.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
    private fun insertMultiplePrevMed(prevMedInfo: List<PreviousMedication>) {
        val email = intent.getStringExtra("email")
        val otpHash = intent.getStringExtra("otpHash")
        val patientID = intent.getIntExtra("patientID", 0)


        val prevMedService = PreviousMedInstance.retrofitBuilder
        prevMedInfo.forEach { prevMedInfo ->
            prevMedService.insertPrevMed(prevMedInfo).enqueue(object : Callback<PreviousMedication> {
                override fun onResponse(call: Call<PreviousMedication>, response: Response<PreviousMedication>) {
                    if (response.isSuccessful) {
                        // Successfully inserted the medication history
//                        Toast.makeText(this@RegisterPrevMedicine, "Added Previous Medication! $otpHash", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegisterPrevMedicine, BodyMassActivity::class.java)
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

                override fun onFailure(call: Call<PreviousMedication>, t: Throwable) {
                    // Handle network or other exceptions
                    Log.d("MainActivity", "Failed to connect: : " + t.message)
                }
            })
        }
    }

}