package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.hartcheck.Model.Patients
import com.example.hartcheck.Model.Users
import com.example.hartcheck.Remote.PatientsRemote.PatientsInstance
import com.example.hartcheck.Remote.PatientsRemote.PatientsInterface
import com.example.hartcheck.Remote.UsersRemote.UsersInstance
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var gGivenName: TextView
    private lateinit var gFamilyName: TextView
    private lateinit var gEmail: TextView

    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val btn_register = findViewById<Button>(R.id.btn_register)
        gGivenName = findViewById(R.id.input_fn)
        gFamilyName = findViewById(R.id.input_ln)
        gEmail = findViewById(R.id.input_email)
        genderDropdown()
//        GoogleSigned()

        //needs helper and observer to add 2nd register page
        //"register btn should be hidden"
        //google sign in register need to fix

//        autoComplete.onItemClickListener = AdapterView.OnItemClickListener{
//            adapterView, view, i , l ->
//
//            val itemSelected = adapterView.getItemAtPosition(i)
//        }



        btn_register.setOnClickListener {
            insertPatient()
//            goSignOut()
//            val intent = Intent(this, RegisterFinActivity::class.java)
//            startActivity(intent)
        }

        //when press next user would be inserted intent the email
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


}