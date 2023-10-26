package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class EducationalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_educational)
        val userID = intent.getIntExtra("userID", 0)
        val btnBackEduc = findViewById<Button>(R.id.btn_back_Educ)

        //back btn implement
        btnBackEduc.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("userID", userID)
            startActivity(intent)
        }
    }
}