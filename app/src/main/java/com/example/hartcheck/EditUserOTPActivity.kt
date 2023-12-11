package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.hartcheck.Data.OTPVerification
import com.example.hartcheck.Remote.UsersRemote.UsersInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditUserOTPActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_otpactivity)

        val btnuserVerifyOTP: Button = findViewById(R.id.btn_verify_user_OTP)

        val userID = intent.getIntExtra("userID", 0)
        val patientID = intent.getIntExtra("patientID", 0)
        val patientName = intent.getStringExtra("patientName")
        Toast.makeText(this, "$userID, $patientID, $patientName", Toast.LENGTH_LONG).show()

        btnuserVerifyOTP.setOnClickListener {
//            verifyOTP()
            replaceFragment(EditProfileFragment.newInstance(userID, patientName!!))

        }
    }
    private fun verifyOTP(){
        val email = intent.getStringExtra("email")
        val otpHash = intent.getStringExtra("otpHash")
        val userID = intent.getIntExtra("userID", 0)
        val patientID = intent.getIntExtra("patientID", 0)
        val patientName = intent.getStringExtra("patientName")

        val txtOTP1 = findViewById<EditText>(R.id.txt_otp_user_1)
        val txtOTP2 = findViewById<EditText>(R.id.txt_otp_user_2)
        val txtOTP3 = findViewById<EditText>(R.id.txt_otp_user_3)
        val txtOTP4 = findViewById<EditText>(R.id.txt_otp_user_4)

        if (txtOTP1.text.isNotEmpty() && txtOTP2.text.isNotEmpty() && txtOTP3.text.isNotEmpty() && txtOTP4.text.isNotEmpty()) {
            val otp = "${txtOTP1.text}${txtOTP2.text}${txtOTP3.text}${txtOTP4.text}"

            val Verify  = OTPVerification(otp = otp, email = email, otpHash = otpHash)
            val OTPVerifyService = UsersInstance.retrofitBuilder
            OTPVerifyService.VerifyOTP(Verify).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>){
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditUserOTPActivity, "OTP Verified", Toast.LENGTH_SHORT).show()
                        replaceFragment(EditProfileFragment.newInstance(userID, patientName!!))

//                        val intent = Intent(this@EditUserOTPActivity, NewPassActivity::class.java)
//                        intent.putExtra("email", email)
//                        intent.putExtra("otpHash", otpHash)
//                        intent.putExtra("userID", userID)
//                        intent.putExtra("patientID", patientID)
//                        intent.putExtra("patientName", patientName)
//                        startActivity(intent)
                    }
                    else {
                        Toast.makeText(this@EditUserOTPActivity, "Registration DENIED", Toast.LENGTH_SHORT).show()
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
//            Toast.makeText(this, "Your OTP that you enter is: $otp", Toast.LENGTH_SHORT).show()

        } else {
            // Show a message or handle the error
        }

    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}