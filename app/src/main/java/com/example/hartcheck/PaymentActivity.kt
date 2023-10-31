package com.example.hartcheck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class PaymentActivity : AppCompatActivity() {

    private lateinit var input_email:EditText
    private lateinit var btn_pay:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        input_email = findViewById(R.id.txt_email_payment)
        btn_pay = findViewById(R.id.btn_send_payment)

        //payment logic here
    }
}