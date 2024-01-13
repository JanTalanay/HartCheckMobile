package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.hartcheck.Data.PaymentBook
import com.example.hartcheck.Remote.PatientsRemote.PatientsInstance
import com.example.hartcheck.Remote.PaymentRemote.PaymentInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentActivity : AppCompatActivity() {// need to fix the UI for automation message

    private lateinit var input_email:EditText
    private lateinit var btn_pay:Button
    var patientEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        btn_pay = findViewById(R.id.btn_send_payment)
        getpatientEmail()
        btn_pay.setOnClickListener {
            paymentBooking()
        }

        //payment logic here
    }
    private fun paymentBooking(){
        val selectedDateTime = intent.getStringExtra("selectedDateTime")
        val userID = intent.getIntExtra("userID", 0)
        val patientID = intent.getIntExtra("patientID", 0)
        val patientName = intent.getStringExtra("patientName")

//        input_email = findViewById(R.id.txt_email_payment)
        val Paymentservice = PaymentInstance.retrofitBuilder
        val emailBook = patientEmail ?: ""

        val paymentInfo = PaymentBook(email = emailBook)

        Paymentservice.paymentBook(paymentInfo).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>){
                if (response.isSuccessful) {
                    Log.d("MainActivity", "Wrong: " + response.code())
                    val intent = Intent(this@PaymentActivity, ConfirmActivity::class.java)
                    intent.putExtra("BUTTON_STATE","payment_success")
                    intent.putExtra("userID", userID)
                    intent.putExtra("patientID", patientID)
                    intent.putExtra("patientName", patientName)
                    intent.putExtra("selectedDateTime", selectedDateTime)
                    startActivity(intent)
//                    Toast.makeText(this@PaymentActivity, "Auto", Toast.LENGTH_LONG).show()

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
    private fun getpatientEmail(){
        val patientID = intent.getIntExtra("patientID", 0)
        val service = PatientsInstance.retrofitBuilder

        service.getEmailByPatientId(patientID).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    patientEmail = response.body()
//                    Toast.makeText(this@PaymentActivity, "$patientEmail", Toast.LENGTH_LONG).show()

//                    val textView = findViewById<TextView>(R.id.view_test)
//                    textView.text = "Patient Email: $patientEmail"
                } else {
                    Log.d("MainActivity", "Failed to connect: " + response.code())
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("MainActivity", "Failed to connect: : " + t.message)
            }
        })
    }

}