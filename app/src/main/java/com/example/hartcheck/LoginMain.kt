package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.hartcheck.Model.Login
import com.example.hartcheck.Model.Patients
import com.example.hartcheck.Model.Users
import com.example.hartcheck.Plugin.Sheets
import com.example.hartcheck.Remote.PatientsRemote.PatientsInstance
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
import java.util.regex.Pattern

class LoginMain : AppCompatActivity() {
    private lateinit var googleSignIn: ImageButton
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    private lateinit var sheets: Sheets
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_main)

        //Logged in as google needs to be fix to get userID
        //register as google is good
        sheets = Sheets()
        googleSignIn = findViewById(R.id.btn_GoogleSignIn)
        val btn_login = findViewById<Button>(R.id.btn_login_main)
        val noAccbtn = findViewById<TextView>(R.id.txt_dont_have_acc)
        val forgotPass = findViewById<TextView>(R.id.txt_forgot)

        googleSignInAccount()

        btn_login.setOnClickListener {
            sheets.loginData(this)
            loginPatient()
        }
        googleSignIn.setOnClickListener{
            sheets.loginData(this)
            goToSignIn()

        }
        noAccbtn.setOnClickListener {
            val intent = Intent(this@LoginMain, RegisterActivity::class.java)
            startActivity(intent)
        }
        forgotPass.setOnClickListener {
            val intent = Intent(this@LoginMain, ForgotActivity::class.java)
            startActivity(intent)
        }
    }

    private fun googleSignInAccount(): GoogleSignInAccount? {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(this, gso)

        val account: GoogleSignInAccount? = GoogleSignIn
            .getLastSignedInAccount(this)
        if(account!=null){
            goToHome(account.email, account.givenName, account.familyName)
        }
        return account
    }

    private fun goToSignIn() {
        val signInIntent = gsc.signInIntent
        startActivityForResult(signInIntent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==1000){
            val task: Task<GoogleSignInAccount> = GoogleSignIn
                .getSignedInAccountFromIntent(data)

            try{
                val account = task.getResult(ApiException::class.java)
                GoogleLoginUserID(account.email!!, account.givenName, account.familyName)
            }
            catch (e:java.lang.Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun goToHome(email: String?, firstName: String?, lastName: String?) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("email", email)
        intent.putExtra("firstName", firstName)
        intent.putExtra("familyName", lastName)
        val patientName = "$firstName $lastName"
        intent.putExtra("patientName", patientName)
        startActivity(intent)
        finish()
    }

    private fun loginPatient() {
        val editEmail = findViewById<EditText>(R.id.input_email)
        val editPassword = findViewById<EditText>(R.id.input_pass)

        val email = editEmail.text.toString()
        val Password = editPassword.text.toString()

        // Check if email and password fields are not empty
        if (email.isEmpty() || Password.isEmpty()) {
            Toast.makeText(this@LoginMain, "The email and password fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate email format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this@LoginMain, "The email is in the incorrect format", Toast.LENGTH_SHORT).show()
            return
        }


        val loginRequest  = Login(email = email, password = Password)
        val loginUser = UsersInstance.retrofitBuilder

        loginUser.loginUser(loginRequest).enqueue(object : Callback<Users> {//CLEAN CODE
        override fun onResponse(call: Call<Users>, response: Response<Users>){
            if (response.isSuccessful) {
                val user = response.body()
                if(user != null){
                    val userID = user.usersID
                    val firstName = user.firstName
                    val lastName = user.lastName
                    val patientName = "$firstName $lastName"
                    if(userID != null){
//                        Toast.makeText(this@LoginMain, "Logged In $userID", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginMain, HomeActivity::class.java)
                        intent.putExtra("userID", userID)
                        intent.putExtra("patientName", patientName)
                        intent.putExtra("firstName", firstName)
                        startActivity(intent)
                    }else {
                        Toast.makeText(this@LoginMain, "Connection Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else {
                Toast.makeText(this@LoginMain, "The email and/or password is incorrect", Toast.LENGTH_SHORT).show()
            }
        }
            override fun onFailure(call: Call<Users>, t: Throwable) {
                Log.d ("MainActivity", "Registration failed: ")
            }
        })
    }
    private fun GoogleLoginUserID(email: String, firstName: String?, lastName: String?) {
        val service = UsersInstance.retrofitBuilder
        service.getRegisterEmail(email).enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    val userID = user?.usersID
                    if (userID != null) {
                        val intent = Intent(this@LoginMain, HomeActivity::class.java)
                        intent.putExtra("userID", userID)
                        intent.putExtra("email", email)
                        intent.putExtra("firstName", firstName)
                        intent.putExtra("familyName", lastName)
                        val patientName = "$firstName $lastName"
                        intent.putExtra("patientName", patientName)
                        startActivity(intent)
                    } else {
                        Log.d("MainActivity", "User ID is null")
                    }
                } else {
                    Log.d("MainActivity", "Failed to connect: " + response.code())
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                Log.d("MainActivity", "Failed to connect: : " + t.message)
            }
        })
    }

}