package com.example.hartcheck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AppointmentDetailsActivity : AppCompatActivity() {

    private lateinit var btn_request:Button
    private lateinit var btn_cancel_sched:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_details)

        btn_request = findViewById(R.id.btn_request_appointment)
        btn_cancel_sched = findViewById(R.id.btn_cancel_appointment)
    }
}