package com.example.zenzoneapp

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar

class AppointmentNoticesAdapter(private val appointments: List<Appointment>) : RecyclerView.Adapter<AppointmentNoticesAdapter.AppointmentViewHolder>() {


    class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appointmentTime: TextView = itemView.findViewById(R.id.AppointmentTime)
        val appointmentLocation: TextView = itemView.findViewById(R.id.AppointmentLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.appointment_list_item_home, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.appointmentLocation.text = appointment.location // Assuming the Appointment class has a 'name' property
        holder.appointmentTime.text = appointment.time // Assuming the Appointment class has a 'time' property

    }

    override fun getItemCount(): Int {
        return appointments.size
    }

    private fun showAppointmentDialog(context: Context, appointmentItem: Appointment) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.appointmet_details_popup, null)

        // Create the dialog
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        // Set appointment details in the dialog
        val therapyNameTextView: TextView = dialogView.findViewById(R.id.TherapyName)
        therapyNameTextView.text = appointmentItem.therapist

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



