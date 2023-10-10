package com.example.hartcheck

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hartcheck.Model.BugReport
import com.example.hartcheck.Model.Users
import com.example.hartcheck.Remote.BugReportRemote.BugReportInstance
import com.example.hartcheck.Remote.UsersRemote.UsersInstance
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_get: Button = findViewById<Button>(R.id.btn_get)
        val btn_insert: Button = findViewById<Button>(R.id.btn_insert)
        val btn_update: Button = findViewById<Button>(R.id.btn_update)
        val btn_delete: Button = findViewById<Button>(R.id.btn_delete)
//        val btn_login: Button = findViewById<Button>(R.id.btn_login)
//        val btn_signup: Button = findViewById<Button>(R.id.btn_SignUp)


        btn_get.setOnClickListener{
            getPatient()
        }
        btn_insert.setOnClickListener {
            insertPatient()
        }
    }

    private fun insertPatient() {
        val emailEditText = findViewById<EditText>(R.id.edit_email)
        val passwordEditText = findViewById<EditText>(R.id.edit_password)
        val firstNameEditText = findViewById<EditText>(R.id.edit_first_name)
        val lastNameEditText = findViewById<EditText>(R.id.edit_last_name)
        val birthdateEditText = findViewById<EditText>(R.id.edit_birthdate)
        val genderEditText = findViewById<EditText>(R.id.edit_gender)
        val phoneNumberEditText = findViewById<EditText>(R.id.edit_phonenumber)
        val role = 0

        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val firstName = firstNameEditText.text.toString()
        val lastName = lastNameEditText.text.toString()
        val birthdate = birthdateEditText.text.toString()
        val gender = if (genderEditText.text.toString().isNotEmpty()) genderEditText.text.toString().toInt() else null
        val phoneNumber = if (phoneNumberEditText.text.toString().isNotEmpty()) phoneNumberEditText.text.toString().toLong() else null

        val usersInput = Users(
//            usersID = null,
            email = email,
            password = password,
            firstName = firstName,
            lastName = lastName,
            birthdate = birthdate,
            gender = gender,
            phoneNumber = phoneNumber,
            role = role
        )


        val userService = UsersInstance.retrofitBuilder
        userService.registerUser(usersInput).enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful) {
                    // Successfully deleted the bug report
                    Log.d("MainActivity", "connected: ")
                } else {
                    // Handle the error response
                    Log.d("MainActivity", "HATDOG: ")
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                // Handle network or other exceptions
            }
        })
    }


    private fun getPatient() {
        val service = UsersInstance.retrofitBuilder
        service.getRegisterUsers().enqueue(object : Callback<List<Users>> {
            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                if (response.isSuccessful) {
                    response.body()?.let { users ->
                        val user = users.firstOrNull()
                        if (user != null) {
                            val emailEditText = findViewById<EditText>(R.id.edit_email)
                            val passwordEditText = findViewById<EditText>(R.id.edit_password)
                            val firstNameEditText = findViewById<EditText>(R.id.edit_first_name)
                            val lastNameEditText = findViewById<EditText>(R.id.edit_last_name)
                            val birthdateEditText = findViewById<EditText>(R.id.edit_birthdate)
                            val genderEditText = findViewById<EditText>(R.id.edit_gender)
                            val phoneNumberEditText = findViewById<EditText>(R.id.edit_phonenumber)

                            emailEditText.setText(user.email)
                            passwordEditText.setText(user.password)
                            firstNameEditText.setText(user.firstName)
                            lastNameEditText.setText(user.lastName)
                            birthdateEditText.setText(user.birthdate.toString())
                            genderEditText.setText(user.gender.toString())
                            phoneNumberEditText.setText(user.phoneNumber.toString())
                            Log.d("MainActivity", "connected: ")
                        }
                    }
                } else {
                    Log.d("MainActivity", "Failed to connect: " + response.code())
                }
            }

            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                Log.d ("MainActivity", "Failed to connect: : " + t.message)
            }
        })
    }
}