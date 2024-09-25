package com.example.zenzoneapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecommendedScheduleAdapter(
    private val context: Context,
    private val recommendedList: List<RecommendedScheduleData>,
    private val onAddClick: (RecommendedScheduleData) -> Unit // Lambda for adding activities
) : RecyclerView.Adapter<RecommendedScheduleAdapter.ViewHolder>() {

    // ViewHolder class for holding the views
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.tvName)
        val descriptionTextView: TextView = view.findViewById(R.id.tvDes)
        val addButton: Button = view.findViewById(R.id.addButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the item layout
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recommended_schedule_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the current recommended schedule
        val recommendedSchedule = recommendedList[position]

        // Bind data to the views
        holder.nameTextView.text = recommendedSchedule.name
        holder.descriptionTextView.text = recommendedSchedule.description

        // Handle the Add Activity button click
        holder.addButton.setOnClickListener {
            onAddClick(recommendedSchedule)
        }
    }

    override fun getItemCount(): Int {
        return recommendedList.size
    }
}
