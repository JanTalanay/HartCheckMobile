package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.hartcheck.Data.OTPVerification
import com.example.hartcheck.Remote.UsersRemote.UsersInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterOTPActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_otpactivity)

        val verifyRegisterOTP = findViewById<Button>(R.id.btn_verify_register_OTP)

        verifyRegisterOTP.setOnClickListener {
            verifyOTP()
        }
    }
    private fun verifyOTP(){
        val email = intent.getStringExtra("email")
        val otpHash = intent.getStringExtra("otpHash")

        val txtRegisterOTP1 = findViewById<EditText>(R.id.txt_register_otp_1)
        val txtRegisterOTP2 = findViewById<EditText>(R.id.txt_register_otp_2)
        val txtRegisterOTP3 = findViewById<EditText>(R.id.txt_register_otp_3)
        val txtRegisterOTP4 = findViewById<EditText>(R.id.txt_register_otp_4)
//
        if (txtRegisterOTP1.text.isNotEmpty() && txtRegisterOTP2.text.isNotEmpty() && txtRegisterOTP3.text.isNotEmpty() && txtRegisterOTP4.text.isNotEmpty()) {
            val registerOtp = "${txtRegisterOTP1.text}${txtRegisterOTP2.text}${txtRegisterOTP3.text}${txtRegisterOTP4.text}"
//
            val Verify  = OTPVerification(otp = registerOtp, email = email, otpHash = otpHash)
            val OTPVerifyService = UsersInstance.retrofitBuilder
            OTPVerifyService.VerifyOTP(Verify).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>){
                    if (response.isSuccessful) {
//                        val responseBody = response.body()?.string()
//                        val jsonObject = JSONObject(responseBody)
//                        val otpHash = jsonObject.getString("OtpHash")
                        Toast.makeText(this@RegisterOTPActivity, "Account Verified", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegisterOTPActivity, LoginMain::class.java)
//                        intent.putExtra("email", email)
//                        intent.putExtra("otpHash", otpHash)
//                        intent.putExtra("otp", registerOtp)
                        startActivity(intent)
                    }
                    else {
                        Toast.makeText(this@RegisterOTPActivity, "Registration DENIED", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this, "Your OTP that you enter is: $registerOtp", Toast.LENGTH_SHORT).show()

        } else {
            // Show a message or handle the error
        }

    }
    private fun backup(){
//        val intent = Intent(this@NewPassActivity, ConfirmActivity::class.java)
//        intent.putExtra("BUTTON_STATE","btn_NewPass")
//        intent.putExtra("isForgot",true)
//        startActivity(intent)
    }
}