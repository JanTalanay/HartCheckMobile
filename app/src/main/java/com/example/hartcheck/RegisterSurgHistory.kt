package com.example.hartcheck

import android.app.DatePickerDialog
import android.app.Dialog
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterSurgHistory : AppCompatActivity() {

    private val calendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_surg_history)
        val inputFields = findViewById<LinearLayout>(R.id.inputFields3)
        val inputFieldsDate = findViewById<LinearLayout>(R.id.inputFieldsDate3)

        val addButton = findViewById<Button>(R.id.btn_addSurg)
        addButton.setOnClickListener {
            if (inputFields.childCount < 5 && inputFieldsDate.childCount <5){
                showModal()
            }

        }

        val removeButton = findViewById<Button>(R.id.btn_removeSurg)
        removeButton.setOnClickListener {
            if (inputFields.childCount > 1 && inputFieldsDate.childCount > 1) {
                inputFields.removeViewAt(inputFields.childCount - 1)
                inputFieldsDate.removeViewAt(inputFieldsDate.childCount - 1)
            }
        }

        //this is where you get values
        val sendBtn = findViewById<Button>(R.id.btn_medHis_surg)
        sendBtn.setOnClickListener {
            val values = mutableListOf<String>()
            val dates = mutableListOf<String>()

        }
    }
    private fun showModal(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.popup_register_surg_history)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btn_close:Button = dialog.findViewById(R.id.btn_modal_deny_surg)
        val btn_confirm = dialog.findViewById<Button>(R.id.btn_modal_confirm_surg)
        val input_med = dialog.findViewById<EditText>(R.id.edit_surg)
        val input_date = dialog.findViewById<EditText>(R.id.edit_prev_surg_date)

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
            val inputFields = findViewById<LinearLayout>(R.id.inputFields3)
            val inputFieldsDate = findViewById<LinearLayout>(R.id.inputFieldsDate3)
            val inputField = TextView(this)
            val inputFieldDate = TextView(this)
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

}