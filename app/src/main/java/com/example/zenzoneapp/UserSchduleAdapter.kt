package com.example.zenzoneapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserSchduleAdapter(private val activityList: List<UserScheduleData>) :
    RecyclerView.Adapter<UserSchduleAdapter.ActivityViewHolder>() {

    class ActivityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvDes: TextView = view.findViewById(R.id.tvDes)
        val addButton: Button = view.findViewById(R.id.addButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_sechdule_list_item, parent, false)
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activityItem = activityList[position]
        holder.tvName.text = activityItem.name
        holder.tvDes.text = activityItem.description
        holder.addButton.setOnClickListener {
            // Handle Add Activity button click here
        }
    }

    override fun getItemCount(): Int = activityList.size
}
