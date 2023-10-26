package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.hartcheck.Model.Patients
import com.example.hartcheck.Model.Users
import com.example.hartcheck.Remote.PatientsRemote.PatientsInstance
import com.example.hartcheck.Remote.UsersRemote.UsersInstance
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    private lateinit var gsc: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val userID = intent.getIntExtra("userID", 0)
//        val patientID = intent.getIntExtra("patientID", 0)

        val btn_bp = findViewById<Button>(R.id.btn_view_bp)
        val btn_consul = findViewById<Button>(R.id.btn_consul)
        val btn_chat = findViewById<Button>(R.id.btn_chat)
        val btn_profile = findViewById<Button>(R.id.btn_profile)
        val btn_info = findViewById<Button>(R.id.btn_info)
        val li_faq = findViewById<TextView>(R.id.li_faq)

//        GoogleSignInOptions()


        btn_info.setOnClickListener {
            val intent = Intent(this, EducationalActivity::class.java)
            intent.putExtra("userID", userID)
            startActivity(intent)
//            intent.putExtra("patientID", patientID)
//            GoogleSignOut()
        }
        li_faq.setOnClickListener {
            val intent = Intent(this, FAQ::class.java)
            intent.putExtra("userID", userID)
            startActivity(intent)
//            intent.putExtra("patientID", patientID)
        }


        btn_bp.setOnClickListener {
            startNextActivity("btn_bp")
        }

        btn_consul.setOnClickListener {
            startNextActivity("btn_consul")
        }

        btn_chat.setOnClickListener {
            startNextActivity("btn_chat")
        }

        btn_profile.setOnClickListener {
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

    fun startNextActivity(buttonState: String) {
        val userID = intent.getIntExtra("userID", 0)
        getPatientID(userID) { patientID ->
            val intent = Intent(this, NavActivity::class.java)
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
    private fun GoogleLoginUserID() { //to be fix
        val email = intent.getStringExtra("email") ?: ""
        val service = UsersInstance.retrofitBuilder
        service.getRegisterEmail(email).enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    val userID = user?.usersID
                    Toast.makeText(this@HomeActivity, "Logged In $userID", Toast.LENGTH_SHORT).show()


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
