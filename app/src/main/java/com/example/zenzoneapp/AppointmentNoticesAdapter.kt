package com.example.zenzoneapp

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
        val openIcon: ImageView = view.findViewById(R.id.openIcon) // Assuming you've added an id for the icon
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.appointment_list_item_home, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointmentItem = appointmentItems[position]
        holder.tvName.text = appointmentItem.name
        holder.tvTime.text = appointmentItem.time
        holder.tvLocation.text = appointmentItem.location

        // Set click listener for the open icon to show the dialog
        holder.openIcon.setOnClickListener {
            showAppointmentDialog(holder.itemView.context, appointmentItem)
        }
    }

    override fun getItemCount() = appointmentItems.size

    private fun showAppointmentDialog(context: Context, appointmentItem: AppointmentItem) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.appointmet_details_popup, null)

        // Create the dialog
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        // Set appointment details in the dialog
        val therapyNameTextView: TextView = dialogView.findViewById(R.id.TherapyName)
        therapyNameTextView.text = appointmentItem.name

        val appointmentTimeTextView: TextView = dialogView.findViewById(R.id.AppointmentTime)
        appointmentTimeTextView.text = appointmentItem.time

        val appointmentLocationTextView: TextView = dialogView.findViewById(R.id.AppointmentLocation)
        appointmentLocationTextView.text = appointmentItem.location

        // Show the dialog
        dialog.show()

        // Handle the close button
        val closeButton: ImageView = dialogView.findViewById(R.id.txtclose)
        closeButton.setOnClickListener {
            dialog.dismiss()
        }
    }

}


data class AppointmentItem(
    val name: String,
    val time: String,
    val location: String
)

