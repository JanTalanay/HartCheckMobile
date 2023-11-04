package com.example.hartcheck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.hartcheck.Model.BloodPressureThreshold
import com.example.hartcheck.Model.Users
import com.example.hartcheck.Remote.BloodPressureRemote.BloodPressureInstance
import com.example.hartcheck.Remote.BloodPressureThresholdRemote.BloodPressureThresholdInstance
import com.example.hartcheck.Remote.ConsultationRemote.ConsultationInstance
import com.example.hartcheck.Remote.DoctorScheduleRemote.DoctorScheduleInstance
import com.example.hartcheck.Remote.PatientsDoctorRemote.PatientsDoctorInstance
import com.example.hartcheck.Wrapper.DoctorInfoList
import com.example.hartcheck.Wrapper.DoctorScheduleDates
import com.example.hartcheck.Wrapper.PatientsDoctorAssign
import com.example.hartcheck.Wrapper.PatientsDoctorList
import com.example.hartcheck.Wrapper.PrevBloodPressure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.await

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val testView: TextView = findViewById(R.id.view_test)
        val editTest: EditText = findViewById(R.id.edit_test)
        val editTest2: EditText = findViewById(R.id.edit_test2)
        val editTest3: EditText = findViewById(R.id.edit_test3)
        val editTest4: EditText = findViewById(R.id.edit_test4)
        val editTest5: EditText = findViewById(R.id.edit_test5)
        val editTest6: EditText = findViewById(R.id.edit_test6)
        val editTest7: EditText = findViewById(R.id.edit_test7)
        val testGet: Button = findViewById(R.id.btn_test)
        val testUpdate: Button = findViewById(R.id.btn_test2)
        val testDelete: Button = findViewById(R.id.btn_test3)
        val testInsert: Button = findViewById(R.id.btn_test4)

        val patientID = intent.getIntExtra("patientID", 0)
//        val userID = intent.getIntExtra("userID", 0)
//        testView.text = userID.toString()

//        Log.d("TestActivity", "User ID: $userID")

        testGet.setOnClickListener {
//            getPatientID()
//            getDoctorID()
//            getPrevBP()
//            getDoctorSchedulesForPatient()
//            getConsultationAssign()
//            getBPThreshold()
//            getPatientsDoctorList()
//            GetDoctorsByPatientId()
        }

    }

    private fun GetDoctorsByPatientId() {
        val patientID = intent.getIntExtra("patientID", 0)
        val service = PatientsDoctorInstance.retrofitBuilder

        service.getDoctorsByPatientId(patientID).enqueue(object : Callback<DoctorInfoList> {
            override fun onResponse(call: Call<DoctorInfoList>, response: Response<DoctorInfoList>) {
                if (response.isSuccessful) {
                    val healthCareProfessionals = response.body()
                    if (healthCareProfessionals != null) {
                        for (professional in healthCareProfessionals.DoctorInfo) {
                            Log.d("TestActivity", "DoctorIDs: ${professional.doctorID}, first: ${professional.firstName}, last ${professional.lastName}")
                        }
                    }
                } else {
                    Log.d("TestActivity", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DoctorInfoList>, t: Throwable) {
                Log.d("TestActivity", "Failure: ${t.message}")
            }
        })

    }

    private fun getBPThreshold() {
        val patientID = intent.getIntExtra("patientID", 0)
        val service = BloodPressureThresholdInstance.retrofitBuilder

        service.getBloodPressureThreshold(patientID).enqueue(object : Callback<BloodPressureThreshold> {
            override fun onResponse(call: Call<BloodPressureThreshold>, response: Response<BloodPressureThreshold>) {
                if(response.isSuccessful){
                    val BPThreshold = response.body()
                    if(BPThreshold != null){
                        Log.d ("MainActivity", "Set Threshold: ${BPThreshold.systolicLevel} and ${BPThreshold.diastolicLevel}")
                    }
                }
                else{
                    Log.d("MainActivity", "Error response: ${response.body()}")
                }

            }
            override fun onFailure(call: Call<BloodPressureThreshold>, t: Throwable) {
                Log.d ("MainActivity", "Failed to connect: : " + t.message)
                if (t is HttpException) {
                    val errorResponse = t.response()?.errorBody()?.string()
                    Log.d("MainActivity", "Error response: $errorResponse")
                }
            }
        })
    }

    private fun getDoctorID(){
        val patientID = intent.getIntExtra("patientID", 0)
        val service = PatientsDoctorInstance.retrofitBuilder

        service.getHealthCareProfessionals(patientID).enqueue(object : Callback<PatientsDoctorAssign> {
            override fun onResponse(call: Call<PatientsDoctorAssign>, response: Response<PatientsDoctorAssign>) {
                if (response.isSuccessful) {
                    val healthCareProfessionals = response.body()
                    if (healthCareProfessionals != null) {
                        for (professional in healthCareProfessionals.HealthCareName) {
                            Log.d("TestActivity", "First Name: ${professional.firstName}, Last Name: ${professional.lastName}")
                        }
                    }
                } else {
                    Log.d("TestActivity", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<PatientsDoctorAssign>, t: Throwable) {
                Log.d("TestActivity", "Failure: ${t.message}")
            }
        })
    }
    private fun getPrevBP(){
        val patientID = intent.getIntExtra("patientID", 0)
        val service = BloodPressureInstance.retrofitBuilder

        service.getBloodPressureID(patientID).enqueue(object : Callback<PrevBloodPressure> {
            override fun onResponse(call: Call<PrevBloodPressure>, response: Response<PrevBloodPressure>) {
                if (response.isSuccessful) {
                    response.body()?.let { prevBP ->
                        // Parse the response data into your PrevBloodPressure object
                        val systolic = prevBP.PrevBP.map { it.systolic }
                        val diastolic = prevBP.PrevBP.map { it.diastolic }
                        val dateTaken = prevBP.PrevBP.map { it.dateTaken }

                        // Display the systolic, diastolic, and dateTaken in a TextView
                        val textView = findViewById<TextView>(R.id.view_test)
                        textView.text = "Systolic: $systolic, Diastolic: $diastolic, Date Taken: $dateTaken"
                    }
                } else {
                    // Handle the error
                    Log.d("MainActivity", "Failed to connect: " + response.code())
                }
            }

            override fun onFailure(call: Call<PrevBloodPressure>, t: Throwable) {
                // Handle the failure
                Log.d("MainActivity", "Failed to connect: : " + t.message)
            }
        })
    }
    private fun getDoctorSchedulesForPatient() {
        val patientID = intent.getIntExtra("patientID", 0)
        val service = DoctorScheduleInstance.retrofitBuilder

        service.getDoctorSchedulesForPatient(patientID).enqueue(object : Callback<DoctorScheduleDates> {
            override fun onResponse(call: Call<DoctorScheduleDates>, response: Response<DoctorScheduleDates>) {
                if (response.isSuccessful) {
                    val doctorSchedules = response.body()
                    if (doctorSchedules != null) {
                        for (doctorSchedule in doctorSchedules.DoctorDates) {
                            Log.d("TestActivity", "Doctor Schedule ID: ${doctorSchedule.doctorSchedID}, Doctor ID: ${doctorSchedule.doctorID}, Schedule Date Time: ${doctorSchedule.schedDateTime}")
                        }
                    }
                } else {
                    Log.d("TestActivity", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DoctorScheduleDates>, t: Throwable) {
                Log.d("TestActivity", "Failure: ${t.message}")
            }
        })
    }
    private fun getConsultationAssign() {
        val patientID = intent.getIntExtra("patientID", 0)
        val service = ConsultationInstance.retrofitBuilder

        service.getConsultationAssign(patientID).enqueue(object : Callback<DoctorScheduleDates> {
            override fun onResponse(call: Call<DoctorScheduleDates>, response: Response<DoctorScheduleDates>) {
                if (response.isSuccessful) {
                    val doctorSchedules = response.body()
                    if (doctorSchedules != null) {
                        val doctorIds = doctorSchedules.DoctorDates.map { it.doctorID }
                        GlobalScope.launch(Dispatchers.Main) {
                            getDoctorInfo(doctorIds)
                        }
                    }
                } else {
                    Log.d("TestActivity", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DoctorScheduleDates>, t: Throwable) {
                Log.d("TestActivity", "Failure: ${t.message}")
            }
        })
    }
    private fun getPatientsDoctorList(){
        val patientID = intent.getIntExtra("patientID", 0)
        val service = PatientsDoctorInstance.retrofitBuilder

        service.getPatientsPatientID(patientID).enqueue(object : Callback<PatientsDoctorList> {
            override fun onResponse(call: Call<PatientsDoctorList>, response: Response<PatientsDoctorList>) {
                if (response.isSuccessful) {
                    val healthCareProfessionals = response.body()
                    if (healthCareProfessionals != null) {
                        for (professional in healthCareProfessionals.PatientsDoctor) {
                            Log.d("TestActivity", "DoctorIDs: ${professional.doctorID}")
                        }
                    }
                } else {
                    Log.d("TestActivity", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<PatientsDoctorList>, t: Throwable) {
                Log.d("TestActivity", "Failure: ${t.message}")
            }
        })
    }
    private suspend fun getDoctorInfo(doctorIDs: List<Int?>) {
        val service = ConsultationInstance.retrofitBuilder
        val doctorsInfo = mutableListOf<Users>()

        for (doctorID in doctorIDs) {
            val doctorInfo = service.getConsultationDoctor(doctorID!!).await()
            if (doctorInfo != null) {
                doctorsInfo.add(doctorInfo)
                Log.d("TestActivity", "${doctorInfo.firstName}, ${doctorInfo.lastName}")
            }
        }
    }

}