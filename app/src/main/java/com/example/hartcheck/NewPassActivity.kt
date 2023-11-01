package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.hartcheck.Data.ChangePassword
import com.example.hartcheck.Data.OTPVerification
import com.example.hartcheck.Remote.UsersRemote.UsersInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewPassActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_pass)

//        val btnBack: Button = findViewById(R.id.btn_back)
        val btnReset: Button = findViewById(R.id.btn_reset_pass)


        btnReset.setOnClickListener {
            changePassword()
        }
    }
    private fun changePassword(){
        val email = intent.getStringExtra("email")
        val otpHash = intent.getStringExtra("otpHash")
        val otp = intent.getStringExtra("otp")

        val txtNewPass: EditText = findViewById(R.id.txt_new_pass)
        val txtConfirmPass: EditText = findViewById(R.id.txt_confirm_newpass)

        val NewPass = txtNewPass.text.toString()
        val ConfirmPass = txtConfirmPass.text.toString()

        if(NewPass == ConfirmPass){
            val changePass  = ChangePassword(email = email, otp = otp, otpHash = otpHash, newPassword = NewPass)
            val ChangePasswordService = UsersInstance.retrofitBuilder
            ChangePasswordService.ChangePassword(changePass).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>){
                    if (response.isSuccessful) {
//                        val responseBody = response.body()?.string()
//                        val jsonObject = JSONObject(responseBody)
//                        val otpHash = jsonObject.getString("OtpHash")
                        Toast.makeText(this@NewPassActivity, "Password Change", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@NewPassActivity, ConfirmActivity::class.java)
                        intent.putExtra("BUTTON_STATE","btn_NewPass")
                        intent.putExtra("isForgot",true)
                        startActivity(intent)
//                        val intent = Intent(this@ForgotActivity, OTPActivity::class.java)
//                        intent.putExtra("email", email)
//                        intent.putExtra("otpHash", otpHash)
//                        startActivity(intent)
                    }
                    else {
                        Toast.makeText(this@NewPassActivity, "Registration DENIED", Toast.LENGTH_SHORT).show()
                        response.errorBody()?.let { errorBody ->
                            Log.d("MainActivity", "Response: ${errorBody.string()}")

                        }
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d ("MainActivity", "Registration failed: ")
                }
            })
        }
        else{

        }
    }
}