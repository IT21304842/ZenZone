package com.example.zenzoneapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserScheduleAdapter(
    private val context: Context,
    private val scheduleList: List<UserScheduleData>,
    private val onEditClick: (UserScheduleData) -> Unit,
    private val onDeleteClick: (UserScheduleData) -> Unit
) : RecyclerView.Adapter<UserScheduleAdapter.UserScheduleViewHolder>() {

    // ViewHolder class for binding the UI components
    inner class UserScheduleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvDes: TextView = view.findViewById(R.id.tvDes)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserScheduleViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_sechdule_list_item, parent, false)
        return UserScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserScheduleViewHolder, position: Int) {
        val scheduleData = scheduleList[position]

        // Bind data to the views
        holder.tvName.text = scheduleData.name
        holder.tvDes.text = scheduleData.description

        // Handle edit button click
        holder.btnEdit.setOnClickListener {
            onEditClick(scheduleData)
        }

        // Handle delete button click
        holder.btnDelete.setOnClickListener {
            onDeleteClick(scheduleData)
        }
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }
}
