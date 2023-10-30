package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.hartcheck.Model.BugReport
import com.example.hartcheck.Remote.BugReportRemote.BugReportInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BugReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bug_report)
        featureDropdown()
        val btn_bug_report:Button = findViewById(R.id.btn_report_prob)
        btn_bug_report.setOnClickListener {
            confirmActivity("btn_bug_reported")
            bugInsert()
        }
    }

    fun confirmActivity(buttonState: String) {
            val intent = Intent(this, ConfirmActivity::class.java)
            intent.putExtra("BUTTON_STATE", buttonState)
            startActivity(intent)
    }
    private fun featureDropdown() {
        val options = listOf("Register", "Login/logout", "Account Management", "Viewing of Appointment", "Blood Pressure Monitoring",
            "Educational Resource", "Blood Pressure Data Sharing", "Chatting System")
        val input_bug_feature = findViewById<Spinner>(R.id.input_bug_feature)
        val adapter = ArrayAdapter(
            this,
            R.layout.app_list_item,
            options.toMutableList().apply { add(0, "Features") })
        adapter.setDropDownViewResource(R.layout.app_list_item)
        input_bug_feature.adapter = adapter
    }

    private fun bugInsert() {
        val userID = intent.getIntExtra("userID", 0)
        val editDesc = findViewById<EditText>(R.id.input_report_details)
        val editFeature = findViewById<Spinner>(R.id.input_bug_feature)
        val options = listOf("Register", "Login/logout", "Account Management", "Viewing of Appointment", "Blood Pressure Monitoring",
            "Educational Resource", "Blood Pressure Data Sharing", "Chatting System")
//        val input_bug_feature = findViewById<Spinner>(R.id.input_bug_feature)
        val adapter = ArrayAdapter(
            this,
            R.layout.app_list_item,
            options.toMutableList().apply { add(0, "Features") })
        adapter.setDropDownViewResource(R.layout.app_list_item)
        editFeature.adapter = adapter


//        val bugUserID = editUserID.text.toString()
//        val bugFeature = editFeature.text.toString()
        val bugDesc = editDesc.text.toString()
        val bugFeature = if (editFeature.selectedItem.toString().isNotEmpty()) {
            when (editFeature.selectedItem.toString()) {
                "Register" -> 0
                "Login/logout" -> 1
                "Account Management" -> 2
                "Viewing of Appointment" -> 3
                "Blood Pressure Monitoring" -> 4
                "Educational Resource" -> 5
                "Blood Pressure Data Sharing" -> 6
                "Chatting System" -> 7
                else -> null
            }
        } else null
        val bugInfo = BugReport( usersID = userID, description = bugDesc, featureID = bugFeature?.toInt())

        val bugService = BugReportInstance.retrofitBuilder
        bugService.insertBugReport(bugInfo).enqueue(object : Callback<BugReport> {
            override fun onResponse(call: Call<BugReport>, response: Response<BugReport>) {
                if (response.isSuccessful) {
                    // Successfully deleted the bug report
                    Toast.makeText(this@BugReportActivity, "Bug Reported", Toast.LENGTH_SHORT).show()
                    Log.d("MainActivity", "Response: ${response.body()}")



                } else {
                    // Handle the error response
                    Log.d("MainActivity", "Response: ${response.body()}")

                }
            }

            override fun onFailure(call: Call<BugReport>, t: Throwable) {
                // Handle network or other exceptions
                Log.d("MainActivity", "Response:" + t.message)

            }
        })
    }
}