package com.example.hartcheck.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hartcheck.Data.DocData
import com.example.hartcheck.R

class AppointmentAdapter(
    private val doctorList: MutableList<DocData>,
    private val frag:Boolean,
    private val patientID: Int?,
    private val patientName: String?,
    private val userID: Int?,
    private val activityClass: Class<*>
) : RecyclerView.Adapter<AppointmentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.appoint_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val doc = doctorList[position]
        holder.bind(doc)
    }

    override fun getItemCount():Int {
        return doctorList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.txt_doc_hist_name)
        private val contentTextView: TextView = itemView.findViewById(R.id.txt_appointment_hist_date)
        private val docLine : ImageView = itemView.findViewById(R.id.doc_line)
        private val appointTitle: TextView = itemView.findViewById(R.id.txt_appointment_title)
        private val viewBtn:TextView = itemView.findViewById(R.id.txt_doc_hist_view)

        fun bind(doc: DocData) {
            titleTextView.text = doc.name

            if(!frag) run {
                docLine.visibility = View.GONE
                appointTitle.visibility = View.GONE
                contentTextView.visibility = View.VISIBLE
            }
            contentTextView.text = doc.appointmentDate
            viewBtn.setOnClickListener {
                val position = adapterPosition
                val selectedDoctor = doctorList[position]
                val intent = Intent(itemView.context, activityClass)
                intent.putExtra("selectedDoctor", selectedDoctor)
                intent.putExtra("doctorID", selectedDoctor.doctorID)
                intent.putExtra("patientID", patientID)
                intent.putExtra("patientName", patientName)
                intent.putExtra("userID", userID)
                itemView.context.startActivity(intent)

            }


        }
    }
}