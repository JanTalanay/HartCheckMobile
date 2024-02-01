package com.example.hartcheck.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hartcheck.Data.ConsultationData
import com.example.hartcheck.R

class ConsultationAdapter(private val consultationList: List<ConsultationData>) : RecyclerView.Adapter<ConsultationAdapter.ViewHolder>()  {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.title_consul_TextView)
        val contentLayout: LinearLayout = itemView.findViewById(R.id.content_consul_Layout)
        val contentTextView: TextView = itemView.findViewById(R.id.content_consul_TextView)

        init {
            titleTextView.setOnClickListener {
                val position = adapterPosition
                if (contentLayout.visibility == View.GONE) {
                    contentLayout.visibility = View.VISIBLE
                } else {
                    contentLayout.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.consul_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val consul = consultationList[position]
        holder.titleTextView.text = consul.title
        holder.contentTextView.text = consul.desc
    }

    override fun getItemCount(): Int {
        return consultationList.size
    }


}