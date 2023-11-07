package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.hartcheck.Model.Patients
import com.example.hartcheck.Remote.PatientsRemote.PatientsInstance
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    private lateinit var gsc: GoogleSignInClient
//    private lateinit var userLoggedIn: TextView
    private lateinit var token: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val userID = intent.getIntExtra("userID", 0)
        val firstName = intent.getStringExtra("firstName")

        val btnBP = findViewById<Button>(R.id.btn_view_bp)
        val btnConsul = findViewById<Button>(R.id.btn_consul)
        val btnChat = findViewById<Button>(R.id.btn_chat)
        val btnProfile = findViewById<Button>(R.id.btn_profile)
        val btnInfo = findViewById<Button>(R.id.btn_info)
        val li_faq = findViewById<TextView>(R.id.li_faq)
        val reportProblem = findViewById<TextView>(R.id.report_problem)
        val userLoggedIn = findViewById<TextView>(R.id.txt_home)

        if(firstName != null){
            userLoggedIn.setText("Hello $firstName What would you like to do?")
        }
        else{
            userLoggedIn.setText("What would you like to do?")
        }

        btnInfo.setOnClickListener {
            val intent = Intent(this, EducationalActivity::class.java)
            intent.putExtra("userID", userID)
            intent.putExtra("firstName", firstName)
            startActivity(intent)
        }
        li_faq.setOnClickListener {
            val intent = Intent(this, FAQ::class.java)
            intent.putExtra("userID", userID)
            intent.putExtra("firstName", firstName)
            startActivity(intent)
        }
        reportProblem.setOnClickListener {
            val intent = Intent(this, BugReportActivity::class.java)
            intent.putExtra("userID", userID)
            intent.putExtra("firstName", firstName)
            startActivity(intent)
        }


        btnBP.setOnClickListener {
            startNextActivity("btn_bp")
        }

        btnConsul.setOnClickListener {
            startNextActivity("btn_consul")
        }

        btnChat.setOnClickListener {
            startNextActivity("btn_chat")
        }

        btnProfile.setOnClickListener {
            startNextActivity("btn_profile")

        }

    }
    private fun GoogleSignOut() {
        gsc.signOut().addOnSuccessListener {
            startActivity(Intent(this, LoginMain::class.java))
            finish()
        }
    }

    private fun GoogleSignInOptions() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(this, gso)
    }

    private fun startNextActivity(buttonState: String) {
        val userID = intent.getIntExtra("userID", 0)
        getPatientID(userID) { patientID ->
            val intent = Intent(this@HomeActivity, NavActivity::class.java)
            intent.putExtra("BUTTON_STATE", buttonState)
            intent.putExtra("userID", userID)
            intent.putExtra("patientID", patientID)
            startActivity(intent)
        }
    }

    private fun getPatientID(userID: Int, onPatientIDRetrieved: (patientID: Int) -> Unit) {
        val service = PatientsInstance.retrofitBuilder

        service.getPatientsID(userID).enqueue(object : Callback<Patients> {
            override fun onResponse(call: Call<Patients>, response: Response<Patients>) {
                if (response.isSuccessful) {
                    response.body()?.let { patients ->
                        if (userID == patients.usersID) {
                            patients.patientID?.let { onPatientIDRetrieved(it) }
                        } else {
                            Log.d("MainActivity", "Wrong: " + response.code())
                        }
                    }
                } else {
                    Log.d("MainActivity", "Failed to connect: " + response.code())
                }
            }

            override fun onFailure(call: Call<Patients>, t: Throwable) {
                Log.d("MainActivity", "Failed to connect: : " + t.message)
                if (t is HttpException) {
                    val errorResponse = t.response()?.errorBody()?.string()
                    Log.d("MainActivity", "Error response: $errorResponse")
                }
            }
        })
    }
}


//
//    private fun GoogleLoginUserID() { //to be fix
//        val email = intent.getStringExtra("email") ?: ""
//        val service = UsersInstance.retrofitBuilder
//        service.getRegisterEmail(email).enqueue(object : Callback<Users> {
//            override fun onResponse(call: Call<Users>, response: Response<Users>) {
//                if (response.isSuccessful) {
//                    val user = response.body()
//                    val userID = user?.usersID
//                    Toast.makeText(this@HomeActivity, "Logged In $userID", Toast.LENGTH_SHORT).show()
//
//
//                } else {
//                    Log.d("MainActivity", "Failed to connect: " + response.code())
//                }
//            }
//
//            override fun onFailure(call: Call<Users>, t: Throwable) {
//                Log.d("MainActivity", "Failed to connect: : " + t.message)
//            }
//        })
//    }

