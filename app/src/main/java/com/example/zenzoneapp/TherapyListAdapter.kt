package com.example.zenzoneapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TherapyListAdapter(private val therapyList: List<TherapyItem>) :
    RecyclerView.Adapter<TherapyListAdapter.TherapyViewHolder>() {

    class TherapyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgTitle: ImageView = itemView.findViewById(R.id.imgTitle)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TherapyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.therapy_list_item, parent, false)
        return TherapyViewHolder(view)
    }

    override fun onBindViewHolder(holder: TherapyViewHolder, position: Int) {
        val therapy = therapyList[position]
        holder.tvName.text = therapy.name
        holder.imgTitle.setImageResource(therapy.imageResId)
    }

    override fun getItemCount(): Int {
        return therapyList.size
    }
}
