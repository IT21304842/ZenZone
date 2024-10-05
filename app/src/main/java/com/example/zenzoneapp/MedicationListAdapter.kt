package com.example.zenzoneapp

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class MedicationListAdapter(
    private val medicationList: List<Medication>,
    private val context: Context
) : RecyclerView.Adapter<MedicationListAdapter.MedicationViewHolder>() {

    inner class MedicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val medicationName: TextView = itemView.findViewById(R.id.medication)
        val medicationDose: TextView = itemView.findViewById(R.id.dose)
        val removeButton: ImageButton = itemView.findViewById(R.id.removeButton) // Reference to the remove button
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.medication_list_item, parent, false)
        return MedicationViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicationViewHolder, position: Int) {
        val medication = medicationList[position]
        holder.medicationName.text = medication.name
        holder.medicationDose.text = medication.dose

        // Handle remove button click
        holder.removeButton.setOnClickListener {
            medication.id?.let { it1 -> showRemoveMedicationDialog(it1) } // Pass the medication ID to the dialog
        }

        // Handle edit button click
        holder.itemView.findViewById<ImageButton>(R.id.EditButton).setOnClickListener {
            showEditMedicationDialog(medication)
        }
    }

    override fun getItemCount(): Int {
        return medicationList.size
    }

    private fun showEditMedicationDialog(medication: Medication) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.edit_medication_popup, null)
        val alertDialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        // Initialize dialog views
        val medicationNameInput: TextView = dialogView.findViewById(R.id.textView10)
        val medicationTimeInput: TextView = dialogView.findViewById(R.id.TimeEditText)
        val medicationDoseInput: TextView = dialogView.findViewById(R.id.DoseEditText)
        val changeButton: Button = dialogView.findViewById(R.id.changeButton)

        // Populate the inputs with current medication data
        medicationNameInput.text = medication.name
        medicationTimeInput.text = medication.time // Assuming you have a 'time' property
        medicationDoseInput.text = medication.dose

        // Set up time picker dialog when the medication time input is clicked
        medicationTimeInput.setOnClickListener {
            // Split the current time into hours and minutes
            val timeParts = medication.time.split(":").map { it.toInt() }
            val hour = timeParts[0]
            val minute = timeParts[1]

            // Create TimePickerDialog
            TimePickerDialog(context, { _, selectedHour, selectedMinute ->
                // Format the selected time and update the TextView
                medicationTimeInput.text = String.format("%02d:%02d", selectedHour, selectedMinute)
            }, hour, minute, true).show()
        }

        changeButton.setOnClickListener {
            // Get the updated values
            val updatedName = medicationNameInput.text.toString()
            val updatedTime = medicationTimeInput.text.toString()
            val updatedDose = medicationDoseInput.text.toString()

            // Update the medication in the database
            updateMedicationInDatabase(medication.id!!, updatedName, updatedTime, updatedDose)
            alertDialog.dismiss() // Close the dialog
        }

        alertDialog.show()
    }


    private fun updateMedicationInDatabase(medicationId: String, name: String, time: String, dose: String) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("medications").child(medicationId)

        val updatedMedication = mapOf(
            "name" to name,
            "time" to time,
            "dose" to dose
        )

        databaseRef.updateChildren(updatedMedication).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Optional: Show a success message or update UI
            } else {
                // Optional: Handle errors
            }
        }
    }


    private fun showRemoveMedicationDialog(medicationId: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.remove_medication_popup, null)
        val alertDialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        // Set up the close button in the dialog
        dialogView.findViewById<ImageView>(R.id.txtclose).setOnClickListener {
            alertDialog.dismiss() // Close the dialog
        }

        // Set up the delete button in the dialog
        dialogView.findViewById<Button>(R.id.deleteButton).setOnClickListener {
            deleteMedicationFromDatabase(medicationId) // Call the delete method
            alertDialog.dismiss() // Close the dialog after deleting
        }

        alertDialog.show()
    }


    // Function to delete the medication from the database
    private fun deleteMedicationFromDatabase(medicationId: String) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("medications").child(medicationId)
        databaseRef.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Optional: Show a success message or update UI
            } else {
                // Optional: Handle errors
            }
        }
    }
}
