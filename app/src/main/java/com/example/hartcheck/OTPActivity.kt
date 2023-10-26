package com.example.hartcheck

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hartcheck.Data.ForgotPassword
import com.example.hartcheck.Data.OTPVerification
import com.example.hartcheck.Remote.UsersRemote.UsersInstance
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OTPActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpactivity)

        val btnBackOTP: Button = findViewById(R.id.btn_back_OTP)
        val btnVerifyOTP: Button = findViewById(R.id.btn_verify_OTP)

//        val email = intent.getStringExtra("email")
//        val otpHash = intent.getStringExtra("otpHash")
//        Toast.makeText(this, "$otpHash", Toast.LENGTH_SHORT).show()

        btnBackOTP.setOnClickListener {
            val intent = Intent(this, NewPassActivity::class.java)
            startActivity(intent)
        }
        btnVerifyOTP.setOnClickListener {
            verifyOTP()
        }

    }
    private fun verifyOTP(){
        val email = intent.getStringExtra("email")
        val otpHash = intent.getStringExtra("otpHash")

        val txtOTP1 = findViewById<EditText>(R.id.txt_otp_1)
        val txtOTP2 = findViewById<EditText>(R.id.txt_otp_2)
        val txtOTP3 = findViewById<EditText>(R.id.txt_otp_3)
        val txtOTP4 = findViewById<EditText>(R.id.txt_otp_4)

        if (txtOTP1.text.isNotEmpty() && txtOTP2.text.isNotEmpty() && txtOTP3.text.isNotEmpty() && txtOTP4.text.isNotEmpty()) {
            val otp = "${txtOTP1.text}${txtOTP2.text}${txtOTP3.text}${txtOTP4.text}"

            val Verify  = OTPVerification(otp = otp, email = email, otpHash = otpHash)
            val OTPVerifyService = UsersInstance.retrofitBuilder
            OTPVerifyService.VerifyOTP(Verify).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>){
                    if (response.isSuccessful) {
//                        val responseBody = response.body()?.string()
//                        val jsonObject = JSONObject(responseBody)
//                        val otpHash = jsonObject.getString("OtpHash")
                        Toast.makeText(this@OTPActivity, "OTP Verified", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@OTPActivity, NewPassActivity::class.java)
                        intent.putExtra("email", email)
                        intent.putExtra("otpHash", otpHash)
                        intent.putExtra("otp", otp)
                        startActivity(intent)
                    }
                    else {
                        Toast.makeText(this@OTPActivity, "Registration DENIED", Toast.LENGTH_SHORT).show()
                        response.errorBody()?.let { errorBody ->
                            Log.d("MainActivity", "Response: ${errorBody.string()}")

                        }
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d ("MainActivity", "Registration failed: ")
                }
            })
            // Use otp for verification
            Toast.makeText(this, "Your OTP that you enter is: $otp", Toast.LENGTH_SHORT).show()

        } else {
            // Show a message or handle the error
        }

    }
}