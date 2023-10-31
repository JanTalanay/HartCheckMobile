package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class BookActivity : AppCompatActivity() {

    private lateinit var btn_book: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        btn_book = findViewById(R.id.btn_book_appointment)

        btn_book.setOnClickListener {
            val intent = Intent(this,PaymentActivity::class.java)
            startActivity(intent)

        }
    }
}