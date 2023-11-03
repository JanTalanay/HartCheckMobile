package com.example.hartcheck.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hartcheck.BookActivity
import com.example.hartcheck.Data.DocData
import com.example.hartcheck.R
import com.example.hartcheck.Wrapper.DoctorScheduleDates

class ListAdapter(private val doctorList: MutableList<DocData>, private val frag:Boolean, private val datesAssign: DoctorScheduleDates,
                  private val patientID: Int?
) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

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
        private val docLine : ImageView = itemView.findViewById(R.id.doc_line)
        private val appointTitle: TextView = itemView.findViewById(R.id.txt_appointment_title)
        private val viewBtn:TextView = itemView.findViewById(R.id.txt_doc_view)

        fun bind(doc: DocData) {
            titleTextView.text = doc.name

            if(!frag) run {
                docLine.visibility = View.VISIBLE
                appointTitle.visibility = View.VISIBLE
                contentTextView.visibility = View.VISIBLE
            }
            contentTextView.text = doc.appointmentDate
            viewBtn.setOnClickListener {
                val intent = Intent(itemView.context, BookActivity::class.java)
                intent.putExtra("datesAssign", datesAssign)
                intent.putExtra("patientID", patientID)
                itemView.context.startActivity(intent)
            }

        }
    }
}