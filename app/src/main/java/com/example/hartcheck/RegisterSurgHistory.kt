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
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.hartcheck.Model.MedicalCondition
import com.example.hartcheck.Model.MedicalHistory
import com.example.hartcheck.Remote.MedHisRemote.MedHisInstance
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Random
import kotlin.math.min

class RegisterSurgHistory : AppCompatActivity() {

    private val calendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_surg_history)
        val inputFields = findViewById<LinearLayout>(R.id.inputFields3)
        val inputFieldsDate = findViewById<LinearLayout>(R.id.inputFieldsDate3)

        val patientID = intent.getIntExtra("patientID", 0) // Provide a default value of 0

        val chipGroup: ChipGroup = findViewById(R.id.chipGroup_surg)
        val otherChip: Chip = findViewById(R.id.otherChip_surg)

        val txt_medCond = findViewById<TextView>(R.id.txt_surg)
        val txt_medCondDate = findViewById<TextView>(R.id.txt_surg_date)
        val removeButton = findViewById<Button>(R.id.btn_removeSurg)

        txt_medCond.visibility = View.GONE
        txt_medCondDate.visibility = View.GONE
        removeButton.visibility = View.GONE

        val arrayList = mutableListOf("Appendectomy", "Hysterectomy", "Cesarean section", "Knee arthroscopy")

        val rand = Random()

        for (s in arrayList) {
            val chip = LayoutInflater.from(this).inflate(R.layout.chip_layout, null) as Chip
            chip.text = s
            val id = rand.nextInt()
            chip.id = id
            chip.setOnClickListener {
                if (chip.isChecked && inputFields.childCount < 5 && inputFieldsDate.childCount <5) {
                    chipAdd(chip.text.toString())
                    chip.isChecked
                    txt_medCond.visibility = View.VISIBLE
                    txt_medCondDate.visibility = View.VISIBLE
                    removeButton.visibility = View.VISIBLE
                }else if(!chip.isChecked && inputFields.childCount > 1 && inputFieldsDate.childCount > 1){
                    inputFields.removeViewAt(inputFields.childCount - 1)
                    inputFieldsDate.removeViewAt(inputFieldsDate.childCount - 1)

                }

            }
            chipGroup.addView(chip)
        }

        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val stringBuilder = StringBuilder()
            for (i in checkedIds) {
                val chip = findViewById<Chip>(i)
                stringBuilder.append(", ").append(chip.text)
            }

        }

        otherChip.setOnClickListener {
            otherChip.isChecked = !otherChip.isChecked
            if (inputFields.childCount < 5 && inputFieldsDate.childCount <5){
                txt_medCond.visibility = View.VISIBLE
                txt_medCondDate.visibility = View.VISIBLE
                removeButton.visibility = View.VISIBLE
                chipAdd("")
                otherChip.isChecked = !otherChip.isChecked
            }
        }


        removeButton.setOnClickListener {
            if (inputFields.childCount > 1 && inputFieldsDate.childCount > 1) {
                inputFields.removeViewAt(inputFields.childCount - 1)
                inputFieldsDate.removeViewAt(inputFieldsDate.childCount - 1)
            }
        }

        //this is where you get values
        val sendBtn = findViewById<Button>(R.id.btn_medHis_surg)
        sendBtn.setOnClickListener {
            val medHisInfos = mutableListOf<MedicalHistory>()
            for (i in 0 until min(inputFields.childCount, inputFieldsDate.childCount)) {
                val view: View = inputFields.getChildAt(i)
                val viewDate: View = inputFieldsDate.getChildAt(i)
                if (view is EditText && viewDate is TextView) {
                    val medCond = view.text.toString()
                    val date = viewDate.text.toString()
                    val medHisInfo = MedicalHistory(patientID = patientID, medicalHistory = medCond, date = date)
                    medHisInfos.add(medHisInfo)
                }
            }
            insertMultipleMedHis(medHisInfos)
        }
    }

    private fun chipAdd(txt:String){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_register_surg_history)

        val input_med = dialog.findViewById<EditText>(R.id.edit_surg)
        val input_date = dialog.findViewById<EditText>(R.id.edit_prev_surg_date)

        val inputFields = findViewById<LinearLayout>(R.id.inputFields3)
        val inputFieldsDate = findViewById<LinearLayout>(R.id.inputFieldsDate3)

        val inputField = EditText(this)
        val inputFieldDate = EditText(this)//add yyyy-mm-dd

        inputField.setText(txt)
        inputFieldDate.text = input_date.text

        inputField.hint="Surgery name"
        inputFieldDate.hint="yyyy-mm-dd"

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
    }
//    private fun showModal(){
//        val dialog = Dialog(this)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(true)
//        dialog.setContentView(R.layout.popup_register_surg_history)
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//        val btn_close:Button = dialog.findViewById(R.id.btn_modal_deny_surg)
//        val btn_confirm = dialog.findViewById<Button>(R.id.btn_modal_confirm_surg)
//        val input_med = dialog.findViewById<EditText>(R.id.edit_surg)
//        val input_date = dialog.findViewById<EditText>(R.id.edit_prev_surg_date)
//
//
//        // Check if any checkbox is checked and set the value to the input_med EditText
////        val appendectomyCheckBox = findViewById<CheckBox>(R.id.cb_Appendectomy)
////        val cholecystectomyCheckBox = findViewById<CheckBox>(R.id.cb_Cholecystectomy)
////        val hysterectomyCheckBox = findViewById<CheckBox>(R.id.cb_Hysterectomy)
////        val cesareansectionCheckBox = findViewById<CheckBox>(R.id.cb_Cesarean_section)
////        val kneearthroCheckBox = findViewById<CheckBox>(R.id.cb_knee_arthro)
////        val mastectomyCheckBox = findViewById<CheckBox>(R.id.cb_mastectomy)
//
//
////        if (appendectomyCheckBox.isChecked) {
////            input_med.setText("Appendectomy")
////        } else if (cholecystectomyCheckBox.isChecked) {
////            input_med.setText("Cholecystectomy")
////        } else if (hysterectomyCheckBox.isChecked) {
////            input_med.setText("Hysterectomy")
////        } else if (cesareansectionCheckBox.isChecked) {
////            input_med.setText("Cesarean section")
////        } else if (kneearthroCheckBox.isChecked) {
////            input_med.setText("Knee arthroscopy")
////        } else if (mastectomyCheckBox.isChecked) {
////            input_med.setText("Mastectomy")
////        }
//
//        input_date.setOnClickListener {
//            val datePickerDialog = DatePickerDialog(
//                this, {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
//                    val selectedDate = Calendar.getInstance()
//                    selectedDate.set(year, monthOfYear, dayOfMonth)
//                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//                    val formattedDate = dateFormat.format(selectedDate.time)
//                    input_date.setText("$formattedDate")
//                },
//                calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH),
//                calendar.get(Calendar.DAY_OF_MONTH)
//            )
//            // Show the DatePicker dialog
//            datePickerDialog.show()
//        }
//        btn_confirm.setOnClickListener {
//            val inputFields = findViewById<LinearLayout>(R.id.inputFields3)
//            val inputFieldsDate = findViewById<LinearLayout>(R.id.inputFieldsDate3)
//            val inputField = EditText(this)
//            val inputFieldDate = EditText(this)
//            inputField.text = input_med.text
//            inputFieldDate.text = input_date.text
//
//            inputField.layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            ).apply {
//                gravity = Gravity.CENTER
//            }
//            inputFieldDate.layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            ).apply {
//                gravity = Gravity.CENTER
//            }
//            inputField.setPadding(41,25,25,25)
//            inputFieldDate.setPadding(41,25,25,25)
//            inputField.textSize = 18F
//            inputFieldDate.textSize = 18F
//
//
//            inputFields.addView(inputField)
//            inputFieldsDate.addView(inputFieldDate)
//
//            dialog.dismiss()
//        }
//
//        btn_close.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        dialog.show()
//    }

    private fun insertMultipleMedHis(medHisInfos: List<MedicalHistory>) {
        val email = intent.getStringExtra("email")
        val otpHash = intent.getStringExtra("otpHash")
        val patientID = intent.getIntExtra("patientID", 0)

        val medHisService = MedHisInstance.retrofitBuilder
        medHisInfos.forEach { medHisInfo ->
            medHisService.insertMedHis(medHisInfo).enqueue(object : Callback<MedicalHistory> {
                override fun onResponse(call: Call<MedicalHistory>, response: Response<MedicalHistory>) {
                    if (response.isSuccessful) {
                        // Successfully inserted the medical history
//                        Toast.makeText(this@RegisterSurgHistory, "Added History of Previous Condition $otpHash", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegisterSurgHistory, RegisterPrevMedicine::class.java)
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

                override fun onFailure(call: Call<MedicalHistory>, t: Throwable) {
                    // Handle network or other exceptions
                    Log.d("MainActivity", "Failed to connect: : " + t.message)
                }
            })
        }
    }

}