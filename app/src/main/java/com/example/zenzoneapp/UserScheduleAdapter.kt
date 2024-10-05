package com.example.zenzoneapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView

class UserScheduleAdapter(
    private val fragment: UserSchedule,
    private var scheduleList: List<ActivityData>,
    private val onEditClick: (ActivityData) -> Unit,
    private val onDeleteClick: (ActivityData) -> Unit
) : RecyclerView.Adapter<UserScheduleAdapter.UserScheduleViewHolder>() {

    inner class UserScheduleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvDes: TextView = view.findViewById(R.id.tvDes)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserScheduleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_sechdule_list_item, parent, false)
        return UserScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserScheduleViewHolder, position: Int) {
        val scheduleData = scheduleList[position]

        holder.tvName.text = scheduleData.activityName
        holder.tvDes.text = scheduleData.description

        holder.btnEdit.setOnClickListener {
            val dialog = EditSchedulePopUp(scheduleData) { updatedSchedule ->
                scheduleList = scheduleList.toMutableList().apply { set(position, updatedSchedule) }
                notifyItemChanged(position)
            }
            dialog.show((fragment.requireActivity() as FragmentActivity).supportFragmentManager, "EditScheduleDialog")
        }

        holder.btnDelete.setOnClickListener {
            val dialog = RemoveSchedulaPopUp(scheduleData) { removedSchedule ->
                // Call the fragment method to remove the activity from the database
                fragment.removeActivity(scheduleData.activityId)

                // Remove the item from the list and update the adapter
                scheduleList = scheduleList.toMutableList().apply { remove(removedSchedule) }
                notifyDataSetChanged()  // Notify the adapter that the list has changed
            }
            dialog.show((fragment.requireActivity() as FragmentActivity).supportFragmentManager, "RemoveScheduleDialog")
        }

    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }

    fun updateScheduleList(newList: List<ActivityData>) {
        scheduleList = newList
        notifyDataSetChanged()
    }
}

