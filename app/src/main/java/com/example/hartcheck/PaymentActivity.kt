package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.hartcheck.Data.PaymentBook
import com.example.hartcheck.Remote.PaymentRemote.PaymentInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentActivity : AppCompatActivity() {

    private lateinit var input_email:EditText
    private lateinit var btn_pay:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        btn_pay = findViewById(R.id.btn_send_payment)

        btn_pay.setOnClickListener {
            paymentBooking()
        }

        //payment logic here
    }
    private fun paymentBooking(){
        val selectedDateTime = intent.getStringExtra("selectedDateTime")

        input_email = findViewById(R.id.txt_email_payment)
        val Paymentservice = PaymentInstance.retrofitBuilder
        val emailBook = input_email.text.toString()

        val paymentInfo = PaymentBook(email = emailBook)

        Paymentservice.paymentBook(paymentInfo).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>){
                if (response.isSuccessful) {
                    Log.d("MainActivity", "Wrong: " + response.code())
                    val intent = Intent(this@PaymentActivity, ConfirmActivity::class.java)
                    intent.putExtra("BUTTON_STATE","payment_success")
                    intent.putExtra("selectedDateTime", selectedDateTime)
                    startActivity(intent)
                }
                else {
                    Log.d("MainActivity", "Wrong: " + response.code())
                    }
                }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d ("MainActivity", "Registration failed: ${t}\"")
            }
        })
    }
}