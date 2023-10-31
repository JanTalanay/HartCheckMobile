package com.example.hartcheck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.hartcheck.R

class RescheduleActivity : AppCompatActivity() {

    private lateinit var btn_resched: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reschedule)

        btn_resched = findViewById(R.id.btn_resched_appointment)

    }
}