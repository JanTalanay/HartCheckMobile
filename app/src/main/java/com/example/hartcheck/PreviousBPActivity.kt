package com.example.hartcheck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hartcheck.Adapter.BpAdapter
import com.example.hartcheck.Adapter.ListAdapter
import com.example.hartcheck.Data.BpData
import com.example.hartcheck.Data.DocData

class PreviousBPActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bpAdapter: BpAdapter
    private lateinit var bpList: MutableList<BpData>
//    private lateinit var systolicTextView: TextView
//    private lateinit var diastolicTextView: TextView = itemView.findViewById(R.id.txt_prev_diastolic)
//    private lateinit var dateTextView: TextView = itemView.findViewById(R.id.txt_bp_date)
    private lateinit var delete:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_previous_bpactivity)

        bpList = mutableListOf(
            BpData(120f, 90f,"June 3, 2023"),
            BpData(180f, 100f,"June 4, 2023"),
            BpData(110f, 83f,"June 5, 2023")
        )

        recyclerView = findViewById(R.id.prevBP)
        recyclerView.layoutManager = LinearLayoutManager(this)
        bpAdapter = BpAdapter(bpList)
        recyclerView.adapter = bpAdapter


    }
}