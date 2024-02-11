package com.example.hartcheck.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hartcheck.Data.EducData
import com.example.hartcheck.R

class EducAdapter (private val educList: List<EducData>) : RecyclerView.Adapter<EducAdapter.ViewHolder>()  {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentLayout: LinearLayout = itemView.findViewById(R.id.contentLayout)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)

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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.faq_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val faq = educList[position]
        holder.titleTextView.text = faq.title
        holder.contentTextView.text = faq.info
    }

    override fun getItemCount(): Int {
        return educList.size
    }

}