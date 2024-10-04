package com.example.zenzoneapp

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class AppointmentNoticesAdapter(private val appointments: List<Appointment>) : RecyclerView.Adapter<AppointmentNoticesAdapter.AppointmentViewHolder>() {

    class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val therapyName: TextView = itemView.findViewById(R.id.tvName)
        val appointmentTime: TextView = itemView.findViewById(R.id.AppointmentTime)
        val appointmentLocation: TextView = itemView.findViewById(R.id.AppointmentLocation)
        val openIcon: ImageView = itemView.findViewById(R.id.openIcon) // Add openIcon reference
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.appointment_list_item_home, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.therapyName.text = appointment.therapyName
        holder.appointmentLocation.text = appointment.location
        holder.appointmentTime.text = appointment.time

        // Set onClickListener for openIcon
        holder.openIcon.setOnClickListener {
            // Open the dialog when the icon is clicked
            showAppointmentDialog(holder.itemView.context, appointment)
        }
    }

    override fun getItemCount(): Int {
        return appointments.size
    }

// Inside your AppointmentNoticesAdapter class

    private fun showAppointmentDialog(context: Context, appointmentItem: Appointment) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.appointmet_details_popup, null)

        // Create the dialog
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        // Set appointment details in the dialog
        val therapyNameTextView: TextView = dialogView.findViewById(R.id.TherapyName)
        therapyNameTextView.text = appointmentItem.therapyName

        val appointmentTimeTextView: TextView = dialogView.findViewById(R.id.AppointmentTime)
        appointmentTimeTextView.text = appointmentItem.time

        val appointmentLocationTextView: TextView = dialogView.findViewById(R.id.AppointmentLocation)
        appointmentLocationTextView.text = appointmentItem.location

        val appointmentTherapistTextView: TextView = dialogView.findViewById(R.id.AppointmentTherapist)
        appointmentTherapistTextView.text = appointmentItem.therapist

        // Reference to EditText for notes
        val notesEditText: EditText = dialogView.findViewById(R.id.NotesEditText)
        notesEditText.setText(appointmentItem.notes)

        // Get the Complete Button
        val completeButton: Button = dialogView.findViewById(R.id.CompleteButton)

        // Handle the Complete button click
        completeButton.setOnClickListener {
            // Get the entered notes
            val updatedNotes = notesEditText.text.toString()

            // Update the notes and status in Firebase
            val appointmentId = appointmentItem.id // Assuming you have the ID somewhere in appointmentItem
            val dbRef = FirebaseDatabase.getInstance().getReference("therapy_sessions").child(appointmentId)

            // Update the existing appointment
            val updatedData = mapOf(
                "notes" to updatedNotes,
                "status" to "complete" // Change status to complete
            )

            dbRef.updateChildren(updatedData).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Update successful
                    Toast.makeText(context, "Appointment updated successfully", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } else {
                    // Handle the error
                    Toast.makeText(context, "Failed to update appointment", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Show the dialog
        dialog.show()

        // Handle the close button
        val closeButton: ImageView = dialogView.findViewById(R.id.txtclose)
        closeButton.setOnClickListener {
            dialog.dismiss()
        }
    }


}
