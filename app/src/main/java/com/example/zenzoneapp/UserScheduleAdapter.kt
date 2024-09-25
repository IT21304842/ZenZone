package com.example.zenzoneapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
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
            val dialog = EditSchedulePopUp(scheduleData) { updatedSchedule ->
                // Handle the updated schedule data
                // Here, you should update the original scheduleList or notify the adapter
                // For simplicity, consider notifying the adapter of changes
                // scheduleList[position] = updatedSchedule // Update the data
                // notifyItemChanged(position) // Notify the adapter
            }
            dialog.show((context as FragmentActivity).supportFragmentManager, "EditScheduleDialog")
        }

        // Handle delete button click
        holder.btnDelete.setOnClickListener {
            val dialog = RemoveSchedulaPopUp(scheduleData) { removedSchedule ->
                // Handle removal of the schedule
                // Here, you should remove the schedule from the list and notify the adapter
                // scheduleList.remove(removedSchedule) // Remove the data
                // notifyDataSetChanged() // Notify the adapter
            }
            dialog.show((context as FragmentActivity).supportFragmentManager, "RemoveScheduleDialog")
        }
    }


    override fun getItemCount(): Int {
        return scheduleList.size
    }
}
