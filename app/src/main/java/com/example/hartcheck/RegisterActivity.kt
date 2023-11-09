package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.hartcheck.Model.Users
import com.example.hartcheck.Plugin.Sheets
import com.example.hartcheck.Remote.UsersRemote.UsersInstance
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterActivity : AppCompatActivity() {

    private lateinit var gGivenName: TextView
    private lateinit var gFamilyName: TextView
    private lateinit var gEmail: TextView

    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient

    private lateinit var sheets: Sheets

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val btn_register = findViewById<Button>(R.id.btn_register)
        sheets = Sheets()
        gGivenName = findViewById(R.id.input_fn)
        gFamilyName = findViewById(R.id.input_ln)
        gEmail = findViewById(R.id.input_email)
        genderDropdown()

        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)

        if (account != null) {
            GoogleSigned()
        }


        btn_register.setOnClickListener {
            sheets.registerData(this)
            insertPatient()
//            goSignOut()
        }
    }

    private fun genderDropdown() {
        val options = listOf("Male", "Female")
        val input_gender = findViewById<Spinner>(R.id.input_gender)
        val adapter = ArrayAdapter(
            this,
            R.layout.app_list_item,
            options.toMutableList().apply { add(0, "Gender") })
        adapter.setDropDownViewResource(R.layout.app_list_item)
        input_gender.adapter = adapter
    }

    private fun GoogleSigned() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(this, gso)

        val account: GoogleSignInAccount? = GoogleSignIn
            .getLastSignedInAccount(this)

        if (account != null) {
            gGivenName.text = account.givenName
            gFamilyName.text = account.familyName
            gEmail.text = account.email
        } else {
            goSignOut()
        }
    }
    private fun goSignOut() {
        gsc.signOut().addOnSuccessListener {
            startActivity(Intent(this, LoginMain::class.java))
            finish()
        }
    }

    private fun insertPatient() {
        val emailEditText = findViewById<EditText>(R.id.input_email)
        val passwordEditText = findViewById<EditText>(R.id.txt_register_pass)
        val firstNameEditText = findViewById<EditText>(R.id.input_fn)
        val lastNameEditText = findViewById<EditText>(R.id.input_ln)
        val birthdateEditText = findViewById<EditText>(R.id.input_birthdate)
//        val genderEditText = findViewById<EditText>(R.id.input_gender)
        val options = listOf("Male", "Female")
        val genderEditText = findViewById<Spinner>(R.id.input_gender)
        val adapter = ArrayAdapter(this, R.layout.app_list_item, options.toMutableList().apply { add(0, "Gender") })
        adapter.setDropDownViewResource(R.layout.app_list_item)
        genderEditText.adapter = adapter

        val phoneNumberEditText = findViewById<EditText>(R.id.input_phone)
        val role = 1

        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val firstName = firstNameEditText.text.toString()
        val lastName = lastNameEditText.text.toString()
        val birthdate = birthdateEditText.text.toString()
//        val gender = if (genderEditText.text.toString().isNotEmpty()) genderEditText.text.toString().toInt() else null
        val gender = if (genderEditText.selectedItem.toString().isNotEmpty()) {
            when (genderEditText.selectedItem.toString()) {
                "Male" -> 0
                "Female" -> 1
                else -> null
            }
        } else null
        val phoneNumber = if (phoneNumberEditText.text.toString().isNotEmpty()) phoneNumberEditText.text.toString().toLong() else null

        // Validations
    // Check if the fields are not empty
        if (email.isEmpty()) {
            emailEditText.error = "The email field is required"
            return
        }
        if (password.isEmpty()) {
            passwordEditText.error = "The password field is required"
            return
        }
        if (firstName.isEmpty()) {
            firstNameEditText.error = "The first name field is required"
            return
        }
        if (lastName.isEmpty()) {
            lastNameEditText.error = "The last name field is required"
            return
        }
        if (birthdate.isEmpty()) {
            birthdateEditText.error = "The birthdate field is required"
            return
        }
        if (phoneNumber == null) {
            phoneNumberEditText.error = "The phone number field is required"
            return
        }

    // Check if the fields meet the length requirements
        if (firstName.length < 2) {
            firstNameEditText.error = "The first name must be at least two or more characters"
            return
        }
        if (lastName.length < 2) {
            lastNameEditText.error = "The last name must be at least two or more characters"
            return
        }

    // Check if the email is valid
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "The email should be a valid Email address"
            return
        }

    // Check if the password meets the requirements
        if (password.length < 8) {
            passwordEditText.error = "The password should at least contain 8 characters"
            return
        }
        if (!password.any { it.isDigit() } || !password.any { it.isUpperCase() } || !password.any { it.isLowerCase() }) {
            passwordEditText.error = "The password should at least contain 1 special character one uppercased character, and one numerical number"
            return
        }

    // Check if the phone number meets the requirements
        if (phoneNumber.toString().length != 11) {
            phoneNumberEditText.error = "The phone number should at least eleven numbers"
            return
        }
        if (!phoneNumber.toString().matches("\\d{11}".toRegex())) {
            phoneNumberEditText.error = "Invalid Format Number"
            return
        }

    // Check if the birthdate is not less than 18 years old
        val birthdateCalendar = Calendar.getInstance()
        birthdateCalendar.time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(birthdate)
        val currentDate = Calendar.getInstance()
        val age = currentDate.get(Calendar.YEAR) - birthdateCalendar.get(Calendar.YEAR)
        if (age < 18) {
            birthdateEditText.error = "You must be at least eighteen years old"
            return
        }

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
                    val users = response.body()
                    if(users != null){
                        Log.d("MainActivity", "Server response: ${users}")
                        Toast.makeText(this@RegisterActivity, "Register", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegisterActivity, RegisterFinActivity::class.java)
                        intent.putExtra("userID", users.usersID)
                        intent.putExtra("email", users.email)
                        intent.putExtra("password", users.password)
                        intent.putExtra("otpHash", users.otpHash)
                        startActivity(intent)
                    }else {
                        Toast.makeText(this@RegisterActivity, "User ID is null", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
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
}