package com.example.zenzoneapp

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zenzoneapp.Medication
import com.example.zenzoneapp.R
import com.google.firebase.database.FirebaseDatabase

class MedicationListAdapter(
    private val medicationList: List<Medication>,
    private val context: Context // You need this to create a dialog
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
    }

    override fun getItemCount(): Int {
        return medicationList.size
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
