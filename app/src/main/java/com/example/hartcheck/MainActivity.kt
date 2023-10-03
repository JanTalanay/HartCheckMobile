package com.example.hartcheck

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hartcheck.Model.Users
import com.example.hartcheck.Remote.Repository.UsersRepository
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

class MainActivity : AppCompatActivity() {

    private lateinit var googleSignIn: Button
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_get: Button = findViewById<Button>(R.id.btn_get)
//        val btn_login: Button = findViewById<Button>(R.id.btn_login)
//        val btn_signup: Button = findViewById<Button>(R.id.btn_SignUp)
        googleSignIn = findViewById(R.id.btn_login)
        googleSignInAccount()



        googleSignIn.setOnClickListener{
            goToSignIn()

        }
        btn_get.setOnClickListener{
            getPatient()
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

    private fun getPatient() {
        val service = UsersInstance.retrofitBuilder
        service.getRegisterUsers().enqueue(object : Callback<List<Users>> {
            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                val responseBody = response.body()

                val filteredData = responseBody?.map { user ->
                    // Create a new instance of Users with only the desired fields
                    Users(
                        firstName = user.firstName,
                        lastName = user.lastName,
                        email = user.email
                    )
                }
                val firstNameEditText = findViewById<EditText>(R.id.edt_user_name)
                val emailEditText = findViewById<EditText>(R.id.edit_password)

                if (filteredData != null) {
                    if (filteredData.isNotEmpty()) {
                        // Get the first user from the filteredData list
                        val user = filteredData[0]

                        // Set the text of the EditText widgets
                        firstNameEditText.setText(user.firstName)
//                        lastNameEditText.setText(user.lastName)
                        emailEditText.setText(user.email)

//                        val textViewResult: TextView = findViewById<TextView>(R.id.txtest)
//                        textViewResult.text = responseBody.toString()
                        Log.d("MainActivity", "connected: ")
                    }
                }
            }

            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                Log.d ("MainActivity", "Failed to connect: : " + t.message)
            }
        })
    }
}