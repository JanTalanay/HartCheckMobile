package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.hartcheck.Data.ForgotPassword
import com.example.hartcheck.Model.Login
import com.example.hartcheck.Model.Users
import com.example.hartcheck.Remote.UsersRemote.UsersInstance
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)


        val btnBack: Button = findViewById(R.id.btn_back_forgot)
        val btnSendOTP: Button = findViewById(R.id.btn_send_code)
        val rememberPass: TextView = findViewById(R.id.remember_pass)
        val txtEmailForgot: EditText = findViewById(R.id.txt_email_forgot)



        btnBack.setOnClickListener {
            val intent = Intent(this, LoginMain::class.java)
            startActivity(intent)
        }
        btnSendOTP.setOnClickListener {
//            val intent = Intent(this, OTPActivity::class.java)
//            startActivity(intent)
            forgotPassword()
        }
        rememberPass.setOnClickListener {
            val intent = Intent(this, LoginMain::class.java)
            startActivity(intent)
        }
    }
    private fun forgotPassword() {
        val emailForgot = findViewById<EditText>(R.id.txt_email_forgot)
        val email = emailForgot.text.toString()

        val forgotPass  = ForgotPassword(email = email)
        val forgotPassService = UsersInstance.retrofitBuilder
        forgotPassService.forgotPassword(forgotPass).enqueue(object : Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>){
            if (response.isSuccessful) {

//                Toast.makeText(this@ForgotActivity, "Email Sent to $email", Toast.LENGTH_SHORT).show()
                forgotPass.otpHash = response.body()?.string()
                val intent = Intent(this@ForgotActivity, OTPActivity::class.java)
                intent.putExtra("email", email)
                intent.putExtra("otpHash", forgotPass.otpHash)
                startActivity(intent)
            }
            else {
                Toast.makeText(this@ForgotActivity, "Registration DENIED", Toast.LENGTH_SHORT).show()
                response.errorBody()?.let { errorBody ->
                    Log.d("MainActivity", "Response: ${errorBody.string()}")

                }
            }
        }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d ("MainActivity", "Registration failed: ${t}\"")
            }
        })
    }
}