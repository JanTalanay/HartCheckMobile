package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ForgotActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)


        val btnBack: Button = findViewById<Button>(R.id.btn_back)
        val btnSendOTP: Button = findViewById<Button>(R.id.btn_send)
        val rememberPass: TextView = findViewById<TextView>(R.id.remember_pass)



        btnBack.setOnClickListener {
            val intent = Intent(this, LoginMain::class.java)
            startActivity(intent)
        }
        btnSendOTP.setOnClickListener {
            val intent = Intent(this, OTPActivity::class.java)
            startActivity(intent)
        }
        rememberPass.setOnClickListener {
            val intent = Intent(this, LoginMain::class.java)
            startActivity(intent)
        }
    }
}