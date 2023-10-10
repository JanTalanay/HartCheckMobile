package com.example.hartcheck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import com.example.hartcheck.Model.Users
import com.example.hartcheck.Remote.UsersRemote.UsersInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        //needs helper and observer to add 2nd register page
        //"register btn should be hidden"
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

    private fun getUserID(){
        //get userID then intent to another act
    }
}