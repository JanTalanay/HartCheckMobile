package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.hartcheck.Model.Patients
import com.example.hartcheck.Model.Users
import com.example.hartcheck.Remote.PatientsRemote.PatientsInstance
import com.example.hartcheck.Remote.PatientsRemote.PatientsInterface
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
        val btn_register: Button = findViewById<Button>(R.id.btn_register)

        btn_register.setOnClickListener {
            insertPatient()
//            val intent = Intent(this, RegisterFinActivity::class.java)
//            startActivity(intent)
        }
        //when press next user would be inserted intent the email
    }
    private fun insertPatient() {
        val emailEditText = findViewById<EditText>(R.id.input_email)
        val passwordEditText = findViewById<EditText>(R.id.txt_register_pass)
        val firstNameEditText = findViewById<EditText>(R.id.input_fn)
        val lastNameEditText = findViewById<EditText>(R.id.input_ln)
        val birthdateEditText = findViewById<EditText>(R.id.input_birthdate)
        val genderEditText = findViewById<EditText>(R.id.input_gender)
        val phoneNumberEditText = findViewById<EditText>(R.id.input_phone)
        val role = 1

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
                    val user = response.body()
                    if(user != null){
                        val userID = user.usersID
                        if(userID != null){
                            Log.d("MainActivity", "User ID: $userID")
                            val intent = Intent(this@RegisterActivity, RegisterFinActivity::class.java)
                            intent.putExtra("userID", userID)
                            intent.putExtra("email", email)
                            intent.putExtra("password", password)
                            startActivity(intent)
                        }else {
                            Toast.makeText(this@RegisterActivity, "User ID is null", Toast.LENGTH_SHORT).show()
                        }
                        Toast.makeText(this@RegisterActivity, "Registration Successful", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle the error response
                    Toast.makeText(this@RegisterActivity, "HATDOG", Toast.LENGTH_SHORT).show()
                    Log.d("MainActivity", "HATDOG: ")
                    Log.d("MainActivity", "Response code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                // Handle network or other exceptions
                Log.d("MainActivity", "Exception: ", t)
            }
        })
    }

    private fun getUserID(){
        //get userID then intent to another act
    }
    private fun insertRole(userID: Int){
        val patientsInput = Patients(usersID = userID)

        val patientsTable = PatientsInstance.retrofitBuilder
        patientsTable.insetPatients(patientsInput).enqueue(object : Callback<Patients> {
            override fun onResponse(call: Call<Patients>, response: Response<Patients>) {
                if (response.isSuccessful) {

                    Toast.makeText(this@RegisterActivity, "Patient inserted into patients table", Toast.LENGTH_SHORT).show()

                } else {
                    // Handle the error response
                    Toast.makeText(this@RegisterActivity, "HATDOG", Toast.LENGTH_SHORT).show()
                    Log.d("MainActivity", "HATDOG: ")
                }
            }

            override fun onFailure(call: Call<Patients>, t: Throwable) {
                // Handle network or other exceptions
            }
        })
    }
}