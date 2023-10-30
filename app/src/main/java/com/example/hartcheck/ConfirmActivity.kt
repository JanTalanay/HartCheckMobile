package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class ConfirmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)
        val userID = intent.getIntExtra("userID", 0)
        val btn_back_home: Button = findViewById(R.id.btn_back_home)

        btn_states()

        btn_back_home.setOnClickListener {
            val intent = Intent(this,HomeActivity::class.java)
            intent.putExtra("userID", userID)
            startActivity(intent)
        }


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

            else -> {

            }
        }
    }
}