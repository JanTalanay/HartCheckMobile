package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.hartcheck.Model.Users
import com.example.hartcheck.Remote.UsersRemote.UsersInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_confirmation)
        //intent the inputs here and make a gender checker
        //if male set ispregnant to false ignore and insert it
        //if female ask if the person is currently pregnant

//        val email = intent.getStringExtra("email")
//        val password = intent.getStringExtra("password")
//        val firstName = intent.getStringExtra("firstName")
//        val lastName = intent.getStringExtra("lastName")
//        val birthdate = intent.getStringExtra("birthdate")
//        val gender = intent.getIntExtra("gender", 1)
//        val phoneNumber = intent.getLongExtra("phoneNumber", 1)
//
//        Toast.makeText(this@RegisterConfirmationActivity, "$gender, $email " , Toast.LENGTH_LONG).show()

        val btnConfirmReg = findViewById<Button>(R.id.btn_confirm_register)

        btnConfirmReg.setOnClickListener{
            insertPatient()
        }
    }
    private fun insertPatient() {
        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")
        val firstName = intent.getStringExtra("firstName")
        val lastName = intent.getStringExtra("lastName")
        val birthdate = intent.getStringExtra("birthdate")
        val gender = intent.getIntExtra("gender", 1)
        val radioGroup = findViewById<RadioGroup>(R.id.rGP_confirmation)
        val selectedId = radioGroup.checkedRadioButtonId
        var isPregnant = false
        if (selectedId == R.id.rb_yes) {
            isPregnant = true

            // Show a warning if the user is pregnant
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Warning")
            builder.setMessage("You are currently pregnant.")
            builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }
        val phoneNumber = intent.getLongExtra("phoneNumber", 1)
        val role = 1


        val usersInput = Users(usersID = null, email = email, password = password, firstName = firstName, lastName = lastName, birthdate = birthdate, gender = gender, isPregnant= isPregnant, phoneNumber = phoneNumber,
            role = role
        )
        val userService = UsersInstance.retrofitBuilder
        userService.registerUser(usersInput).enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful) {
                    val users = response.body()
                    if(users != null){
//                        Log.d("MainActivity", "Server response: ${users}")
                        Toast.makeText(this@RegisterConfirmationActivity, "Registered", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegisterConfirmationActivity, RegisterFinActivity::class.java) //change intent to new activity
//                        intent.putExtra("userID", users.usersID)
//                        intent.putExtra("email", users.email)
//                        intent.putExtra("password", users.password)
//                        intent.putExtra("otpHash", users.otpHash)
//                        startActivity(intent)
                    }else {
                        Toast.makeText(this@RegisterConfirmationActivity, "User ID is null", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    // Handle the error response
                    Log.d("MainActivity", "Response code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                // Handle network or other exceptions
                Log.d("MainActivity", "Exception: ", t)
            }
        })
    }
}