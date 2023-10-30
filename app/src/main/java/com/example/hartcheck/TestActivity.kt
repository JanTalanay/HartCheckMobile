package com.example.hartcheck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.hartcheck.Model.Patients
import com.example.hartcheck.Remote.PatientsRemote.PatientsInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val testView: TextView = findViewById<TextView>(R.id.view_test)
        val editTest: EditText = findViewById<EditText>(R.id.edit_test)
        val editTest2: EditText = findViewById<EditText>(R.id.edit_test2)
        val editTest3: EditText = findViewById<EditText>(R.id.edit_test3)
        val editTest4: EditText = findViewById<EditText>(R.id.edit_test4)
        val editTest5: EditText = findViewById<EditText>(R.id.edit_test5)
        val editTest6: EditText = findViewById<EditText>(R.id.edit_test6)
        val editTest7: EditText = findViewById<EditText>(R.id.edit_test7)
        val testGet: Button = findViewById<Button>(R.id.btn_test)
        val testUpdate: Button = findViewById<Button>(R.id.btn_test2)
        val testDelete: Button = findViewById<Button>(R.id.btn_test3)
        val testInsert: Button = findViewById<Button>(R.id.btn_test4)

        val patientID = intent.getIntExtra("patientID", 0)
        val userID = intent.getIntExtra("userID", 0)
        testView.text = userID.toString()

//        Log.d("TestActivity", "User ID: $userID")

        testGet.setOnClickListener {
//            getPatientID()
        }

    }
    private fun getPatientID() {
        val userID = intent.getIntExtra("email", 0)
        val service = PatientsInstance.retrofitBuilder

        service.getPatientsID(userID).enqueue(object : Callback<Patients> {
            override fun onResponse(call: Call<Patients>, response: Response<Patients>) {
                if(response.isSuccessful){
                    response.body()?.let { patients ->
                        if(userID.equals(patients.usersID)){
                            Log.d("MainActivity", "connected: ${patients.patientID}")//Intent
                            Toast.makeText(this@TestActivity, "Registration Successful ${patients.patientID}", Toast.LENGTH_SHORT).show()
                        }else{
                            Log.d("MainActivity", "Wrong: " + response.code())
                        }

//                            val editTest: EditText = findViewById<EditText>(R.id.edit_test)
//                            val editTest2: EditText = findViewById<EditText>(R.id.edit_test2)
//
//                            editTest.setText(patients.patientID.toString())
//                            editTest2.setText(patients.usersID.toString())
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
    }
}