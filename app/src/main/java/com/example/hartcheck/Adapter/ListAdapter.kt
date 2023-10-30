package com.example.hartcheck.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hartcheck.Data.DocData
import com.example.hartcheck.R

class ListAdapter(private val doctorList: List<DocData>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.doctor_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val doc = doctorList[position]
        holder.bind(doc)
    }

    override fun getItemCount(): Int {
        return doctorList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.txt_doc_name)
        private val contentTextView: TextView = itemView.findViewById(R.id.txt_appointment_date)

        fun bind(doc: DocData) {
            titleTextView.text = doc.name
            contentTextView.text = doc.appointmentDate
        }
    }
}