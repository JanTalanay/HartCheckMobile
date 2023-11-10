package com.example.hartcheck

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.CalendarContract
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.hartcheck.Data.DocData
import com.example.hartcheck.Model.Consultation
import com.example.hartcheck.Model.Users
import com.example.hartcheck.Remote.ConsultationRemote.ConsultationInstance
import com.example.hartcheck.Remote.DoctorScheduleRemote.DoctorScheduleInstance
import com.example.hartcheck.Wrapper.DoctorScheduleDates
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookActivity : AppCompatActivity() {
    private lateinit var btn_book: Button
    private lateinit var btnbackbook: Button
    private lateinit var txtdoctorname: TextView
    private lateinit var datesAndIds: List<Pair<String, Int>>
    //    private lateinit var txtBook: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)
        val patientID = intent.getIntExtra("patientID", 0)
        val selectedDoctor = intent.getParcelableExtra<DocData>("selectedDoctor")
        val userID = intent.getIntExtra("userID", 0)
        val patientName = intent.getStringExtra("patientName")


        txtdoctorname = findViewById(R.id.txt_doctor_name)
        btn_book = findViewById(R.id.btn_book_appointment)
        btnbackbook = findViewById(R.id.btn_back_book)

        txtdoctorname.text = selectedDoctor?.name
        btnbackbook.setOnClickListener {
            replaceFragment(ConsultationFragment.newInstance(userID,patientID, patientName!!))
        }
        btn_book.setOnClickListener {
            insertConsultation()
        }
        onDoctorDatesAssigned(patientID) { datesAssign ->
            val selectedDoctorID = selectedDoctor?.doctorID
            val selectedDates = datesAssign.DoctorDates.filter { it.doctorID == selectedDoctorID }
            datesAndIds = selectedDates.map { Pair(formatDateTime(it.schedDateTime!!), it.doctorSchedID!!) }

            val dates = datesAndIds.map { it.first }
            val options = listOf("Features", *dates.toTypedArray())

            val input_bug_feature = findViewById<Spinner>(R.id.input_booking_date)
            val adapter = ArrayAdapter(this, R.layout.app_list_item, options)
            adapter.setDropDownViewResource(R.layout.app_list_item)
            input_bug_feature.adapter = adapter
        }

    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()

        fragmentTransaction?.replace(R.id.frame_layout, fragment)
        fragmentTransaction?.commit()
    }
    private fun insertConsultation() {
        val userID = intent.getIntExtra("userID", 0)
        val patientID = intent.getIntExtra("patientID", 0)
        val patientName = intent.getStringExtra("patientName")

        val input_bug_feature = findViewById<Spinner>(R.id.input_booking_date)
        val selectedDateTime = input_bug_feature.selectedItem.toString()

        val doctorSchedID = datesAndIds.find { it.first == selectedDateTime }?.second

        val Consultationservice = ConsultationInstance.retrofitBuilder
        val ConInfo = Consultation(patientID = patientID, doctorSchedID = doctorSchedID)
        Consultationservice.insertConsultation(ConInfo).enqueue(object : Callback<Consultation> {
            override fun onResponse(call: Call<Consultation>, response: Response<Consultation>) {
                if (response.isSuccessful) {
                    val intent = Intent(this@BookActivity, PaymentActivity::class.java)
                    intent.putExtra("selectedDateTime", selectedDateTime)
                    intent.putExtra("userID", userID)
                    intent.putExtra("patientID", patientID)
                    intent.putExtra("patientName", patientName)
                    startActivity(intent)

                    Log.d("MainActivity", "Response: ${response.body()}")
                }
                else {
                    // Handle the error response
                    Log.d("MainActivity", "Response: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<Consultation>, t: Throwable) {
                // Handle network or other exceptions
                Log.d("MainActivity", "Exception: ", t)
            }
        })
    }
    private fun formatDateTime(originalDateTime: String): String {
        val inputPattern = "yyyy-MM-dd'T'HH:mm:ss"
        val outputPattern = "MMMM dd, yyyy 'at' hh:mm a"

        val inputFormat = SimpleDateFormat(inputPattern, Locale.US)
        val outputFormat = SimpleDateFormat(outputPattern, Locale.US)

        val dateTime = inputFormat.parse(originalDateTime)
        return outputFormat.format(dateTime)
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
    private fun onDoctorDatesAssigned(patientID: Int, onDoctorDatesAssignedRetrieved: (datesAssign: DoctorScheduleDates)-> Unit){

        val doctorSchedService = DoctorScheduleInstance.retrofitBuilder
        doctorSchedService.getDoctorSchedulesForPatient(patientID).enqueue(object : Callback<DoctorScheduleDates>{
            override fun onResponse(call: Call<DoctorScheduleDates>, response: Response<DoctorScheduleDates>) {
                if(response.isSuccessful){
                    response.body()?.let{dateAssign ->
                        onDoctorDatesAssignedRetrieved(dateAssign)
                    }
                }
                else{
                    Log.d("TestActivity", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DoctorScheduleDates>, t: Throwable) {
                Log.d("TestActivity", "Failure: ${t.message}")
            }
        })
    }
}
