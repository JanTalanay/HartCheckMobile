package com.example.hartcheck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.hartcheck.Model.BugReport
import com.example.hartcheck.Remote.BugReportRemote.BugReportInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BugReportAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bug_report_act)

        val btn_getBug: Button = findViewById<Button>(R.id.btn_getBug)
        val btn_deleteBug: Button = findViewById<Button>(R.id.btn_deleteBug)
        val btn_updateBug: Button = findViewById<Button>(R.id.btn_updateBug)
        val btn_insertBug: Button = findViewById<Button>(R.id.btn_insertBug)


        btn_deleteBug.setOnClickListener {
            bugDelete()
        }

        btn_insertBug.setOnClickListener {
            bugInsert()
        }
        btn_getBug.setOnClickListener {
            bugGet()
        }
        btn_updateBug.setOnClickListener {
            bugUpdate()
        }
    }

    private fun bugGet() {
        TODO("Not yet implemented")
    }

    private fun bugUpdate() {
        TODO("Not yet implemented")
    }

    private fun bugInsert() {
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

    private fun bugDelete() {

        val editUserID: EditText = findViewById<EditText>(R.id.edit_userID)
        val editBugID: EditText = findViewById<EditText>(R.id.edit_bugID)
        val editDesc: EditText = findViewById<EditText>(R.id.edit_description)
        val editFeature: EditText = findViewById<EditText>(R.id.edit_feature)
        val bugID = editBugID.text.toString()

        //method for delete
        val bugDelete = BugReportInstance.retrofitBuilder
        bugDelete.deleteBugReports(bugID).enqueue(object : Callback<BugReport> {
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