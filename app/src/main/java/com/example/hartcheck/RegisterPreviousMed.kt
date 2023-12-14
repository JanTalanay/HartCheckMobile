package com.example.hartcheck

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterPreviousMed : AppCompatActivity() {

    private lateinit var tvDatePicker: TextView
    private val calendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_previous_med)
        val inputFields = findViewById<LinearLayout>(R.id.inputFields)
        val inputFieldsDate = findViewById<LinearLayout>(R.id.inputFieldsDate)
        tvDatePicker = findViewById(R.id.txt_medcond_date)


        tvDatePicker.setOnClickListener {
            // Show the DatePicker dialog
            showDatePicker()
        }

        val addButton = findViewById<Button>(R.id.btn_addMed)
        addButton.setOnClickListener {
            if (inputFields.childCount < 5 && inputFieldsDate.childCount <5){
                val inputField = EditText(this)
                val inputFieldDate = TextView(this)
                inputField.hint = this.getString(R.string.input_heredetary)
                inputFieldDate.text = "Date"

                inputField.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                inputFieldDate.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                inputFieldDate.setPadding(41,25,25,25)
                inputFieldDate.isClickable = true
                inputFieldDate.isFocusable = true
                inputFieldDate.textSize = 18F
                inputFieldDate.setOnClickListener {
                    // Show the DatePicker dialog
                    val datePickerDialog = DatePickerDialog(
                        this, {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                            val selectedDate = Calendar.getInstance()
                            selectedDate.set(year, monthOfYear, dayOfMonth)
                            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            val formattedDate = dateFormat.format(selectedDate.time)
                            inputFieldDate.text = "$formattedDate"
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )
                    // Show the DatePicker dialog
                    datePickerDialog.show()
                }


                inputFields.addView(inputField)
                inputFieldsDate.addView(inputFieldDate)
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
        val sendBtn = findViewById<Button>(R.id.btn_medHis_bodyMass)
        sendBtn.setOnClickListener {
            val values = mutableListOf<String>()
            val dates = mutableListOf<String>()
            for (i in 0 until inputFields.childCount) {
                val view: View = inputFields.getChildAt(i)
                if (view is EditText) {
                    values.add(view.text.toString())
                }
            }
            for (i in 0 until inputFieldsDate.childCount) {
                val view: View = inputFieldsDate.getChildAt(i)
                if (view is TextView) {
                    dates.add(view.text.toString())
                }
            }
            for (i in values){
                Log.d("Text", i)
            }
            for (i in dates){
                Log.d("Date", i)
            }
        }
    }
    private fun showDatePicker() {
        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this, {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                tvDatePicker.text = "$formattedDate"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Show the DatePicker dialog
        datePickerDialog.show()
    }
}