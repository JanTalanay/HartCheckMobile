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

//    private lateinit var emailEditText: EditText
//    private lateinit var passwordEditText: EditText
//    private lateinit var firstNameEditText: EditText
//    private lateinit var lastNameEditText: EditText
//    private lateinit var birthdateEditText: EditText
//    private lateinit var genderEditText: Spinner
//    private lateinit var phoneNumberEditText: EditText
//    private val options = listOf("Male", "Female")
//    private var role = 1

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
            Log.d("RegisterActivity", "Before registerData")
            sheets.registerData(this)
            Log.d("RegisterActivity", "After registerData")
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
        val genderEditText = findViewById<Spinner>(R.id.input_gender)
        val isPregnant = false
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

        val phoneNumber = if (phoneNumberEditText.text.toString().isNotEmpty()) phoneNumberEditText.text.toString().toLong()
        else null


        Toast.makeText(this@RegisterActivity, "Gender: $gender, " , Toast.LENGTH_LONG).show()
        if (gender == 0) {
            registerPatientsMale(email, password, firstName, lastName, birthdate, gender, isPregnant, phoneNumber, role)
            return
        }
        if (gender == 1) {
            val intent = Intent(this, RegisterConfirmationActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("password", password)
            intent.putExtra("firstName", firstName)
            intent.putExtra("lastName", lastName)
            intent.putExtra("birthdate", birthdate)
            intent.putExtra("gender", gender)
            intent.putExtra("phoneNumber", phoneNumber)
            startActivity(intent)
            return
        }
    }
    private fun registerPatientsMale(email: String, password: String, firstName: String, lastName: String, birthdate: String, gender: Int?, isPregnant: Boolean, phoneNumber: Long?,
                                     role: Int) {

        val usersInput = Users(
            email = email,
            password = password,
            firstName = firstName,
            lastName = lastName,
            birthdate = birthdate,
            gender = gender,
            isPregnant = isPregnant,
            phoneNumber = phoneNumber,
            role = role
        )

        val userService = UsersInstance.retrofitBuilder
        userService.registerUser(usersInput).enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful) {
                    val users = response.body()
                    if(users != null){
//                        Log.d("MainActivity", "Server response: ${users}")
                        Toast.makeText(this@RegisterActivity, "Register", Toast.LENGTH_SHORT).show()
//                        val intent = Intent(this@RegisterActivity, RegisterFinActivity::class.java) //change intent to new activity
//                        intent.putExtra("userID", users.usersID)
//                        intent.putExtra("email", users.email)
//                        intent.putExtra("password", users.password)
//                        intent.putExtra("otpHash", users.otpHash)
//                        startActivity(intent)
                    }else {
                        Toast.makeText(this@RegisterActivity, "User ID is null", Toast.LENGTH_SHORT).show()
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