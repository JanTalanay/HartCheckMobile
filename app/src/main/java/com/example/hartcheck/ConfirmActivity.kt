package com.example.hartcheck

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.CalendarContract
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ConfirmActivity : AppCompatActivity() {
    private val CHANNEL_ID ="Your_Channel_ID"
    private val notificationID = 101
    private lateinit var gsc: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)
        val userID = intent.getIntExtra("userID", 0)
        val patientID = intent.getIntExtra("patientID", 0)
        val patientName = intent.getStringExtra("patientName")

        val btn_back_home: Button = findViewById(R.id.btn_back_home)
        GoogleSignInOptions()
        btn_states()


        btn_back_home.setOnClickListener {
            val pageState = intent.getBooleanExtra("isForgot", false)

            if(!pageState){
                val intent = Intent(this,HomeActivity::class.java)
                intent.putExtra("userID", userID)
                intent.putExtra("patientID", patientID)
                intent.putExtra("patientName", patientName)
                startActivity(intent)
            }
            else{
                val intent = Intent(this,LoginMain::class.java)
                GoogleSignOut()
                startActivity(intent)
            }
        }


    }
    private fun GoogleSignOut() {
        gsc.signOut().addOnSuccessListener {
            startActivity(Intent(this, LoginMain::class.java))
            finish()
        }
    }

    private fun GoogleSignInOptions() {
        val gso = com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder(com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(this, gso)
    }
    private fun btn_states(){
        val buttonState = intent.getStringExtra("BUTTON_STATE")
        val img_report: ImageView = findViewById(R.id.img_report)
        val header_confirm: TextView = findViewById(R.id.header_confirm)
        val txt_confirm: TextView = findViewById(R.id.txt_confirm)
        val btn_back_home: Button = findViewById(R.id.btn_back_home)
        //val userID = intent.getIntExtra("userID", 0)
        //val patientID = intent.getIntExtra("patientID", 0)

        when (buttonState) {
            "btn_bug_reported" -> {
                header_confirm.setText(R.string.header_bug_reported)
                txt_confirm.setText(R.string.p_admin_review)
            }
            "btn_NewPass" ->{
                header_confirm.setText(R.string.header_pass_change)
                txt_confirm.setText(R.string.p_success_change)
                btn_back_home.setText(R.string.btn_back_log)

            }
            "btn_AccReg" ->{
                header_confirm.setText(R.string.header_acc_verify)
                txt_confirm.setText(R.string.p_account_verified)
            }
            "btn_request" ->{
                header_confirm.setText(R.string.header_request_sent)
                txt_confirm.setText(R.string.p_success_change)
                btn_back_home.setText(R.string.btn_view_app)

            }
            "payment_success" ->{
                header_confirm.setText(R.string.header_purchase_success)
                txt_confirm.setText(R.string.p_booking_success)
                btn_back_home.setText(R.string.btn_view_app)

                Booked()
            }
            "appointment_resched" ->{
                img_report.setImageResource(R.drawable.ic_sad)
                header_confirm.setText(R.string.header_resched)
                txt_confirm.setText(R.string.p_resched_appointment)
                btn_back_home.setText(R.string.btn_view_app)

            }

            else -> {

            }
        }
    }

    private fun Booked() {
        val selectedDateTime = intent.getStringExtra("selectedDateTime")

        val inputPattern = "MMMM dd, yyyy 'at' hh:mm a"
        val inputFormat = SimpleDateFormat(inputPattern, Locale.ENGLISH)
        val dateTime = inputFormat.parse(selectedDateTime)

        val startMillis: Long = dateTime.time
        val endMillis: Long = startMillis + 60 * 60 * 1000

        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
            .putExtra(CalendarContract.Events.TITLE, "Doctor's Check-Up")
            .putExtra(CalendarContract.Events.DESCRIPTION, "Online Meeting")
            .putExtra(CalendarContract.Events.EVENT_LOCATION, "Google/Zoom")
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

    private fun formatDateTime(originalDateTime: String): String {
        val inputPattern = "yyyy-MM-dd'T'HH:mm:ss"
        val outputPattern = "MMMM dd, yyyy 'at' hh:mm a"

        val inputFormat = SimpleDateFormat(inputPattern, Locale.US)
        val outputFormat = SimpleDateFormat(outputPattern, Locale.US)

        val dateTime = inputFormat.parse(originalDateTime)
        return outputFormat.format(dateTime)
    }
}