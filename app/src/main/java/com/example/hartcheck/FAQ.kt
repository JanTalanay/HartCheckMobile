package com.example.hartcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.hartcheck.Data.FAQData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hartcheck.Adapter.ExpandableAdapter

class FAQ : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExpandableAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq)
        val userID = intent.getIntExtra("userID", 0)
        val btnBackFAQ = findViewById<Button>(R.id.btn_back_FAQ)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val faqList = listOf(
            FAQData(getString(R.string.Q1), getString(R.string.ans1)),
            FAQData(getString(R.string.Q2), getString(R.string.ans1)),
            FAQData(getString(R.string.Q3), getString(R.string.ans1)),
            FAQData(getString(R.string.Q4), getString(R.string.ans1)),
            FAQData(getString(R.string.Q5), getString(R.string.ans1))
        )

        adapter = ExpandableAdapter(faqList)
        recyclerView.adapter = adapter
        btnBackFAQ.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("userID", userID)
            startActivity(intent)
        }
    }
}