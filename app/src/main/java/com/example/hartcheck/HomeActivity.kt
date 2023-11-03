package com.example.hartcheck

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.hartcheck.Model.Patients
import com.example.hartcheck.Model.PatientsDoctor
import com.example.hartcheck.Model.Users
import com.example.hartcheck.Remote.ConsultationRemote.ConsultationInstance
import com.example.hartcheck.Remote.DoctorScheduleRemote.DoctorScheduleInstance
import com.example.hartcheck.Remote.PatientsDoctor.PatientsDoctorInstance
import com.example.hartcheck.Remote.PatientsRemote.PatientsInstance
import com.example.hartcheck.Remote.UsersRemote.UsersInstance
import com.example.hartcheck.Wrapper.DoctorScheduleDates
import com.example.hartcheck.Wrapper.PatientsDoctorAssign
import com.example.hartcheck.databinding.DoctorItemBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.await

class HomeActivity : AppCompatActivity() {
    private lateinit var gsc: GoogleSignInClient
    private lateinit var token: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val userID = intent.getIntExtra("userID", 0)

        val btnBP = findViewById<Button>(R.id.btn_view_bp)
        val btnConsul = findViewById<Button>(R.id.btn_consul)
        val btnChat = findViewById<Button>(R.id.btn_chat)
        val btnProfile = findViewById<Button>(R.id.btn_profile)
        val btnInfo = findViewById<Button>(R.id.btn_info)
        val li_faq = findViewById<TextView>(R.id.li_faq)
        val reportProblem = findViewById<TextView>(R.id.report_problem)

        btnInfo.setOnClickListener {
            val intent = Intent(this, EducationalActivity::class.java)
            intent.putExtra("userID", userID)
            startActivity(intent)
        }
        li_faq.setOnClickListener {
            val intent = Intent(this, FAQ::class.java)
            intent.putExtra("userID", userID)
            startActivity(intent)
        }
        reportProblem.setOnClickListener {
            val intent = Intent(this, BugReportActivity::class.java)
            intent.putExtra("userID", userID)
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

