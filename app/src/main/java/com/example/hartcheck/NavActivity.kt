package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.hartcheck.Model.Patients
import com.example.hartcheck.Model.Users
import com.example.hartcheck.Remote.PatientsRemote.PatientsInstance
import com.example.hartcheck.Wrapper.DoctorScheduleDates
import com.example.hartcheck.Wrapper.PatientsDoctorAssign
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
        val userID = intent.getIntExtra("userID", 0)
        val patientID = intent.getIntExtra("patientID", 0)
        val doctorAssign = intent.getParcelableExtra<PatientsDoctorAssign>("doctorAssign")
        val dateAssign = intent.getParcelableExtra<DoctorScheduleDates>("datesAssign")
        val doctorSchedules = intent.getParcelableExtra<DoctorScheduleDates>("doctorSchedules")
        val doctorsInfo = intent.getSerializableExtra("doctorsInfo") as ArrayList<Users>
//        val doctorSchedules = intent.getParcelableExtra<DoctorScheduleDates>("doctorSchedules")
//        val doctorsInfo = intent.getSerializableExtra("doctorsInfo") as ArrayList<Users>


//        val names = doctorSchedules?.DoctorDates?.joinToString(separator = ", ") { "${it.doctorID} ${it.doctorSchedID} ${it.schedDateTime}" }
//        if (doctorsInfo != null) {
//            val doctorsInfoString = StringBuilder()
//            for (doctor in doctorsInfo) {
//                doctorsInfoString.append("Name: ${doctor.firstName} ${doctor.lastName}\n")
//                Log.d("TestActivity", "${doctor.firstName}, ${doctor.lastName}")
//            }
//            Toast.makeText(this, doctorsInfoString.toString(), Toast.LENGTH_LONG).show()
//        } else {
//            Toast.makeText(this, "No doctors info available", Toast.LENGTH_LONG).show()
//        }
//
//        Toast.makeText(this, "$names", Toast.LENGTH_SHORT).show()

        btn_states()
//        val userID = intent.getIntExtra("userID", 0)
//        val patientID = intent.getIntExtra("patientID", 0)

        binding.navBar.setOnItemSelectedListener {
            when(it){
                R.id.nav_profile -> replaceFragment(UserFragment.newInstance(userID))
                R.id.nav_consultations ->
                    if (doctorAssign != null) {
                        replaceFragment(ConsultationFragment.newInstance(userID,patientID,doctorAssign,dateAssign!!, doctorSchedules!!, doctorsInfo))
                    }
                R.id.nav_bp -> replaceFragment(BPFragment.newInstance(userID,patientID))
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
        val doctorAssign = intent.getParcelableExtra<PatientsDoctorAssign>("doctorAssign")
        val dateAssign = intent.getParcelableExtra<DoctorScheduleDates>("datesAssign")
        val doctorSchedules = intent.getParcelableExtra<DoctorScheduleDates>("doctorSchedules")
        val doctorsInfo = intent.getSerializableExtra("doctorsInfo") as ArrayList<Users>

        when (buttonState) {
            "btn_bp" -> {
                replaceFragment(BPFragment.newInstance(userID,patientID))
            }
            "btn_consul" -> {// have to think first
                if (doctorAssign != null) {
                    replaceFragment(ConsultationFragment.newInstance(userID,patientID,doctorAssign,dateAssign!!, doctorSchedules!!, doctorsInfo))
                }
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
    private fun getDoctorUserID(){//use doctorID to get the userID

    }
    private fun getDoctorInfo(){//use userID to get the info assigned to the patient

    }

}