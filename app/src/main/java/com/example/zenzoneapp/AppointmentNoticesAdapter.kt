package com.example.zenzoneapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zenzoneapp.R

class AppointmentNoticesAdapter(
    private val appointmentItems: List<AppointmentItem>
) : RecyclerView.Adapter<AppointmentNoticesAdapter.AppointmentViewHolder>() {

    class AppointmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvTime: TextView = view.findViewById(R.id.AppointmentTime)
        val tvLocation: TextView = view.findViewById(R.id.AppointmentLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.appointment_list_item_home, parent, false) // Make sure this points to your appointment card layout
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointmentItem = appointmentItems[position]
        holder.tvName.text = appointmentItem.name
        holder.tvTime.text = appointmentItem.time
        holder.tvLocation.text = appointmentItem.location
    }

    override fun getItemCount() = appointmentItems.size
}

data class AppointmentItem(
    val name: String,
    val time: String,
    val location: String
)

