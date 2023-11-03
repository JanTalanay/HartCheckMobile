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

class BookActivity : AppCompatActivity() {
    private val CHANNEL_ID ="Your_Channel_ID"
    private val notificationID = 101
    private lateinit var btn_book: Button
    private lateinit var txtdoctorname: TextView
//    private lateinit var txtBook: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        val datesAssign = intent.getParcelableExtra<DoctorScheduleDates>("datesAssign")
        val patientID = intent.getIntExtra("patientID", 0)

//        val names = datesAssign?.DoctorDates?.joinToString(separator = ", ") { "${it.doctorID} ${it.doctorSchedID} ${it.schedDateTime}" }
//        Toast.makeText(this, "GOT UR:$names", Toast.LENGTH_SHORT).show()
//        Toast.makeText(this, "GOT UR:$patientID", Toast.LENGTH_SHORT).show()

        txtdoctorname = findViewById(R.id.txt_doctor_name)
        btn_book = findViewById(R.id.btn_book_appointment)

        btn_book.setOnClickListener {
//            val intent = Intent(this,PaymentActivity::class.java)
//            startActivity(intent)
            insertConsultation()
        }
        featureDropdown()
    }
    private fun Booked() {
        val startMillis: Long = Calendar.getInstance().run {
            set(2023, 10, 19, 7, 30)
            timeInMillis
        }
        val endMillis: Long = Calendar.getInstance().run {
            set(2023, 10, 19, 8, 30)
            timeInMillis
        }
        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
            .putExtra(CalendarContract.Events.TITLE, "Meeting")
            .putExtra(CalendarContract.Events.DESCRIPTION, "Team Meeting")
            .putExtra(CalendarContract.Events.EVENT_LOCATION, "Office")
            .putExtra(
                CalendarContract.Events.AVAILABILITY,
                CalendarContract.Events.AVAILABILITY_BUSY
            )
        startActivity(intent)
        // Check if the event has been created
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            createNotification()
        }, 10000)  // Delay of 10 seconds
    }
    private fun createNotification() {
        val permission = "android.permission.POST_NOTIFICATIONS"
        val hasPermission = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle("Booked Doctor's Appointment")
            .setContentText("You have successfully booked an appointment.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if(hasPermission){
            with(NotificationManagerCompat.from(this)) {
                notify(notificationID, builder.build())
            }
        }
        else{

        }
    }
    private fun insertConsultation() {
        val patientID = intent.getIntExtra("patientID", 0)
        val datesAssign = intent.getParcelableExtra<DoctorScheduleDates>("datesAssign")

        val input_bug_feature = findViewById<Spinner>(R.id.input_booking_date)
        val selectedDateTime = input_bug_feature.selectedItem.toString()
        val doctorSchedID = datesAssign?.DoctorDates?.find { it.schedDateTime == selectedDateTime }?.doctorSchedID

        if (doctorSchedID == null) {
            // Handle the case where the selected schedDateTime does not exist in the DoctorDates list
            Toast.makeText(this, "Invalid date and time selected.", Toast.LENGTH_SHORT).show()
            return
        }

        val Consultationservice = ConsultationInstance.retrofitBuilder
        val ConInfo = Consultation(patientID = patientID, doctorSchedID = doctorSchedID)
        Consultationservice.insertConsultation(ConInfo).enqueue(object : Callback<Consultation> {
            override fun onResponse(call: Call<Consultation>, response: Response<Consultation>) {
                if (response.isSuccessful) {
                    val intent = Intent(this@BookActivity, PaymentActivity::class.java)
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



    private fun featureDropdown() {
        val datesAssign = intent.getParcelableExtra<DoctorScheduleDates>("datesAssign")
        val dates = datesAssign?.DoctorDates?.map { it.schedDateTime }
        val options = listOf("Features", *dates!!.toTypedArray())

        val input_bug_feature = findViewById<Spinner>(R.id.input_booking_date)
        val adapter = ArrayAdapter(this, R.layout.app_list_item, options)
        adapter.setDropDownViewResource(R.layout.app_list_item)
        input_bug_feature.adapter = adapter
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