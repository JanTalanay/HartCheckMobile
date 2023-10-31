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
import com.example.hartcheck.Model.Patients
import com.example.hartcheck.Model.Users
import com.example.hartcheck.Remote.PatientsRemote.PatientsInstance
import com.example.hartcheck.Remote.Token.TokenInstance
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

        //Logged in as google needs to be fix to get userID
        //register as google is good

        googleSignIn = findViewById(R.id.btn_GoogleSignIn)
        val btn_login = findViewById<Button>(R.id.btn_login_main)
        val noAccbtn = findViewById<TextView>(R.id.txt_dont_have_acc)
        val forgotPass = findViewById<TextView>(R.id.txt_forgot)

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
            goToHome(account.email)
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
                goToHome(account.email)

            }
            catch (e:java.lang.Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToHome(email: String?) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
        finish()
    }

    private fun loginPatient() {
        val editEmail = findViewById<EditText>(R.id.input_email)
        val editPassword = findViewById<EditText>(R.id.input_pass)

        val email = editEmail.text.toString()
        val Password = editPassword.text.toString()

        val loginRequest  = Login(email = email, password = Password)
        val loginUser = UsersInstance.retrofitBuilder
        //generate token here


        loginUser.loginUser(loginRequest).enqueue(object : Callback<Users> {//CLEAN CODE
        override fun onResponse(call: Call<Users>, response: Response<Users>){
            if (response.isSuccessful) {
                val user = response.body()
                if(user != null){
                    val userID = user.usersID
                    if(userID != null){
//                        Toast.makeText(this@LoginMain, "Logged In $userID", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginMain, HomeActivity::class.java)
                        intent.putExtra("userID", userID)
                        startActivity(intent)
                    }else {
                        Toast.makeText(this@LoginMain, "User ID is null", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else {
                Toast.makeText(this@LoginMain, "Registration DENIED", Toast.LENGTH_SHORT).show()
            }
        }
            override fun onFailure(call: Call<Users>, t: Throwable) {
                Log.d ("MainActivity", "Registration failed: ")
            }
        })
//        loginUser.loginUser(loginRequest).enqueue(object : Callback<Users> {
//            override fun onResponse(call: Call<Users>, response: Response<Users>) {
//                if (response.isSuccessful) {
//                    handleSuccessfulResponse(response.body())
//                } else {
//                    handleUnsuccessfulResponse()
//                }
//            }
//
//            override fun onFailure(call: Call<Users>, t: Throwable) {
//                Log.d("MainActivity", "Registration failed: ")
//            }
//
//            private fun handleSuccessfulResponse(user: Users?) {
//                if (user != null) {
//                    val userID = user.usersID
//                    if (userID != null) {
//                        if(TokenInstance.isTokenAvailable()){
//                            showToast("Logged In $userID")
//                            startHomeActivity(userID,TokenInstance.getToken())
//                        }else{
//                            TokenInstance.generateToken(16)
//                            startHomeActivity(userID,TokenInstance.getToken())
//                        }
//
//                    } else {
//                        showToast("User ID is null")
//                    }
//                }
//            }
//
//            private fun handleUnsuccessfulResponse() {
//                showToast("Registration DENIED")
//            }
//
//            private fun showToast(message: String) {
//                Toast.makeText(this@LoginMain, message, Toast.LENGTH_SHORT).show()
//            }
//
//            private fun startHomeActivity(userID: Int, token:String?) {
//                val intent = Intent(this@LoginMain, HomeActivity::class.java)
//                intent.putExtra("userID", userID)
//                intent.putExtra("token", token)
//                startActivity(intent)
//            }
//        })
    }
}