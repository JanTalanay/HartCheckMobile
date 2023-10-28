package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.example.hartcheck.Model.Users
import com.example.hartcheck.Remote.UsersRemote.UsersInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

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
    private fun viewEduc() {

    }
}