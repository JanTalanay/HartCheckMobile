package com.example.hartcheck.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hartcheck.Data.BpData
import com.example.hartcheck.R

class BpAdapter(private val bpList: MutableList<BpData>) : RecyclerView.Adapter<BpAdapter.ViewHolder>() {
    //Call actions here from api or etc
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bp_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val doc = bpList[position]
        holder.bind(doc)
    }

    override fun getItemCount(): Int {
        return bpList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val systolicTextView: TextView = itemView.findViewById(R.id.txt_prev_systolic)
        private val diastolicTextView: TextView = itemView.findViewById(R.id.txt_prev_diastolic)
        private val dateTextView:TextView = itemView.findViewById(R.id.txt_bp_date)
        private val deleteTextView: TextView = itemView.findViewById(R.id.txt_bp_delete)

        init {
            deleteTextView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    deleteItem(position)
                }
            }
        }

        fun bind(bp: BpData) {
            systolicTextView.text = bp.systolic.toString()
            diastolicTextView.text = bp.diastolic.toString()
            dateTextView.text =bp.dateAdded
        }
    }
    private fun deleteItem(position: Int) {
        bpList.removeAt(position)
        notifyItemRemoved(position)
    }
}