package com.example.hartcheck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.hartcheck.Model.BugReport
import com.example.hartcheck.Remote.BugReportRemote.BugReportInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterMedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registermed)
    }
    private fun medHisInsert() {
        val editUserID: EditText = findViewById<EditText>(R.id.edit_userID)
        val editBugID: EditText = findViewById<EditText>(R.id.edit_bugID)
        val editDesc: EditText = findViewById<EditText>(R.id.edit_description)
        val editFeature: EditText = findViewById<EditText>(R.id.edit_feature)


        val bugUserID = editUserID.text.toString()
        val bugDesc = editDesc.text.toString()
        val bugFeature = editFeature.text.toString()
        val bugInfo = BugReport(usersID = bugUserID.toInt(), description = bugDesc, featureID = bugFeature.toInt())

        val bugService = BugReportInstance.retrofitBuilder
        bugService.insertBugReport(bugInfo).enqueue(object : Callback<BugReport> {
            override fun onResponse(call: Call<BugReport>, response: Response<BugReport>) {
                if (response.isSuccessful) {
                    // Successfully deleted the bug report

                } else {
                    // Handle the error response
                }
            }

            override fun onFailure(call: Call<BugReport>, t: Throwable) {
                // Handle network or other exceptions
            }
        })
    }
}