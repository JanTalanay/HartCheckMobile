package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.hartcheck.Data.ForgotPassword
import com.example.hartcheck.Data.UserConfirm
import com.example.hartcheck.Remote.UsersRemote.UsersInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConfirmUserEmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_user_email)

        val btnUserOTP: Button = findViewById(R.id.btn_send_user_code)
        val userID = intent.getIntExtra("userID", 0)
        val patientID = intent.getIntExtra("patientID", 0)
        val patientName = intent.getStringExtra("patientName")
        btnUserOTP.setOnClickListener {
//            confirmUser()
            val intent = Intent(this@ConfirmUserEmailActivity, EditUserOTPActivity::class.java)
            intent.putExtra("userID", userID)
            intent.putExtra("patientID", patientID)
            intent.putExtra("patientName", patientName)
            startActivity(intent)
        }
//        val userID = intent.getIntExtra("userID", 0)
//        val patientID = intent.getIntExtra("patientID", 0)
//        val patientName = intent.getStringExtra("patientName")
//        Toast.makeText(this, "$userID, $patientID, $patientName", Toast.LENGTH_LONG).show()
    }
    private fun confirmUser() {
        val emailConfirm = findViewById<EditText>(R.id.txt_email_user_confirm)
        val email = emailConfirm.text.toString()
        val userID = intent.getIntExtra("userID", 0)
        val patientID = intent.getIntExtra("patientID", 0)
        val patientName = intent.getStringExtra("patientName")

        val userConfirm  = UserConfirm(email = email)
        val userConfirmService = UsersInstance.retrofitBuilder
        userConfirmService.confirmUser(userConfirm).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>){
                if (response.isSuccessful) {

//                Toast.makeText(this@ConfirmUserEmailActivity, "Email Sent to $email", Toast.LENGTH_SHORT).show()
                    userConfirm.otpHash = response.body()?.string()
                    val intent = Intent(this@ConfirmUserEmailActivity, EditUserOTPActivity::class.java)
                    intent.putExtra("email", email)
                    intent.putExtra("otpHash", userConfirm.otpHash)
                    intent.putExtra("userID", userID)
                    intent.putExtra("patientID", patientID)
                    intent.putExtra("patientName", patientName)
                    startActivity(intent)
                }
                else {
                    Toast.makeText(this@ConfirmUserEmailActivity, "Registration DENIED", Toast.LENGTH_SHORT).show()
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