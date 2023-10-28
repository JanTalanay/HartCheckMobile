package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.hartcheck.Model.Patients
import com.example.hartcheck.Remote.PatientsRemote.PatientsInstance
import com.example.hartcheck.databinding.ActivityNavBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class NavActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNavBinding
    private lateinit var gsc: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btn_states()
//        val userID = intent.getIntExtra("userID", 0)
//        val patientID = intent.getIntExtra("patientID", 0)
//        Toast.makeText(this, "GOT UR: $userID AND $patientID", Toast.LENGTH_SHORT).show()

        binding.navBar.setOnItemSelectedListener {
            when(it){

                R.id.nav_profile -> replaceFragment(UserFragment())
                R.id.nav_consultations -> replaceFragment(ConsultationFragment())
                R.id.nav_bp -> replaceFragment(BPFragment())
                R.id.nav_chat -> replaceFragment(ChatFragment())
                else ->{

                }
            }
            true
        }


    }
    private fun btn_states(){
        val buttonState = intent.getStringExtra("BUTTON_STATE")
        val userID = intent.getIntExtra("userID", 0)
        val patientID = intent.getIntExtra("patientID", 0)

        when (buttonState) {
            "btn_bp" -> {
                replaceFragment(BPFragment.newInstance(userID,patientID))
            }
            "btn_consul" -> {// have to think first
                replaceFragment(ConsultationFragment())
            }
            "btn_chat" -> {
                replaceFragment(ChatFragment())
            }
            "btn_profile" -> {
                replaceFragment(UserFragment.newInstance(userID))
            }
            else -> {

            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
    private fun getPatientID() {
//        val userID = intent.getIntExtra("email", 0)
//        val service = PatientsInstance.retrofitBuilder
//
//        service.getPatientsID(userID).enqueue(object : Callback<Patients> {
//            override fun onResponse(call: Call<Patients>, response: Response<Patients>) {
//                if(response.isSuccessful){
//                    response.body()?.let { patients ->
//                        if(userID.equals(patients.usersID)){
//                            val intent = Intent(this@NavActivity, NavActivity::class.java)
//                            intent.putExtra("patientID", patients.patientID)
//                            startActivity(intent)
//                        }else{
//                            Log.d("MainActivity", "Wrong: " + response.code())
//                        }
//                    }
//                } else {
//                    Log.d("MainActivity", "Failed to connect: " + response.code())
//
//                }
//            }
//            override fun onFailure(call: Call<Patients>, t: Throwable) {
//                Log.d ("MainActivity", "Failed to connect: : " + t.message)
//                if (t is HttpException) {
//                    val errorResponse = t.response()?.errorBody()?.string()
//                    Log.d("MainActivity", "Error response: $errorResponse")
//                }
//            }
//        })
    }

}