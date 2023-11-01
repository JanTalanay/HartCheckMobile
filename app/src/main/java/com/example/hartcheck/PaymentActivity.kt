package com.example.hartcheck

import android.content.Intent
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

        btn_pay.setOnClickListener {
            val intent = Intent(this, ConfirmActivity::class.java)
            intent.putExtra("BUTTON_STATE","payment_success")
            startActivity(intent)
        }

        //payment logic here
    }
}