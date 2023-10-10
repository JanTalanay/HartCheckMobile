package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.hartcheck.Model.Login
import com.example.hartcheck.Model.Users
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

class LoginMain : AppCompatActivity() {
    private lateinit var googleSignIn: ImageButton
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_main)

        googleSignIn = findViewById(R.id.btn_GoogleSignIn)
        val btn_login: Button = findViewById<Button>(R.id.btn_login_main)
        val noAccbtn: TextView = findViewById<TextView>(R.id.txt_dont_have_acc)
        val forgotPass: TextView = findViewById<TextView>(R.id.txt_forgot)

        googleSignInAccount()

        btn_login.setOnClickListener {
            loginPatient()
        }
        googleSignIn.setOnClickListener{
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
            goToHome()
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
                goToHome()
                task.getResult(ApiException::class.java )

            }
            catch (e:java.lang.Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToHome() {
        val intent = Intent(this, GoogleSign::class.java)
        startActivity(intent)
        finish()
    }

    private fun loginPatient() {
        val editEmail: EditText = findViewById<EditText>(R.id.input_email)
        val editPassword: EditText = findViewById<EditText>(R.id.input_pass)

        val email = editEmail.text.toString()
        val Password = editPassword.text.toString()

        val loginRequest  = Login(email = email, password = Password)
        val loginUser = UsersInstance.retrofitBuilder

        loginUser.loginUser(loginRequest).enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>){
                if (response.isSuccessful) {
                    // Registration successful, handle it as needed
                    val user = response.body()
                    if(user != null){
                        val userID = user.usersID
                        if(userID != null){
                            Toast.makeText(this@LoginMain, "Registration Successful $userID", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@LoginMain, HomeActivity::class.java)
                            startActivity(intent)
                        }else {
                            Toast.makeText(this@LoginMain, "User ID is null", Toast.LENGTH_SHORT).show()
                        }
                        Toast.makeText(this@LoginMain, "Registration Successful", Toast.LENGTH_SHORT).show()
                    }
//                    Log.d ("MainActivity", "Registration successful: ")

                }
                else {
                    // Registration failed, handle the error
                    Toast.makeText(this@LoginMain, "Registration DENIED", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                // Handle network failure
                Log.d ("MainActivity", "Registration failed: ")
            }
        })
    }
}