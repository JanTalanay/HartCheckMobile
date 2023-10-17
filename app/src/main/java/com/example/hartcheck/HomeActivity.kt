package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val btn_bp = findViewById<Button>(R.id.btn_view_bp)
        val btn_consul = findViewById<Button>(R.id.btn_consul)
        val btn_chat = findViewById<Button>(R.id.btn_chat)
        val btn_profile = findViewById<Button>(R.id.btn_profile)

        //add educ btn and faq link
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
    fun startNextActivity(buttonState: String) {
        val intent = Intent(this, NavActivity::class.java)
        intent.putExtra("BUTTON_STATE", buttonState)
        startActivity(intent)
    }
}