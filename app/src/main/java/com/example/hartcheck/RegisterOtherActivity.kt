package com.example.hartcheck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class RegisterOtherActivity : AppCompatActivity() {

    private lateinit var btn_current: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_other)

        btn_current = findViewById(R.id.btn_current_cond)

        btn_current.setOnClickListener {
//            val intent = Intent(this, LoginMain::class.java)
//            startActivity(intent)
        }


    }
}