package com.example.zenzoneapp

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class MedicationDialogFragment : DialogFragment() {

    private lateinit var database: DatabaseReference
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.medication_new, container, false)

        // Initialize Firebase Auth and Database
        val auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("medications")
        userId = auth.currentUser?.uid ?: ""

        // Get references to the EditText fields
        val nameEditText = view.findViewById<EditText>(R.id.NameEditText)
        val timeTextView = view.findViewById<TextView>(R.id.timeTextView) // TextView for time
        val doseEditText = view.findViewById<EditText>(R.id.DoseEditText)

        // Set a click listener to open TimePickerDialog
        timeTextView.setOnClickListener {
            showTimePickerDialog(timeTextView)
        }

        view.findViewById<Button>(R.id.addButton).setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val time = timeTextView.text.toString().trim()
            val dose = doseEditText.text.toString().trim()

            if (name.isEmpty() || time.isEmpty() || dose.isEmpty()) {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create a new medication object
            val medicationId = database.push().key
            val medication = Medication(medicationId, name, time, dose, userId)

            medicationId?.let {
                // Save medication details in the Realtime Database under the user ID
                database.child(userId).child(it).setValue(medication)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Medication added successfully", Toast.LENGTH_SHORT).show()
                        dismiss() // Close the dialog
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Failed to add medication", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        return view
    }

    private fun showTimePickerDialog(timeTextView: TextView) {
        // Get the current time
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Create TimePickerDialog
        val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            // Format the time as HH:mm
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            // Set the selected time in the TextView
            timeTextView.text = formattedTime
        }, hour, minute, true) // Set to true for 24-hour format

        timePickerDialog.show() // Show the dialog
    }
}

