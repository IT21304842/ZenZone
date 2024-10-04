package com.example.zenzoneapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.zenzoneapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class TherapySessionDialog : DialogFragment() {

    private lateinit var therapyDateEditText: EditText
    private lateinit var therapyTimeEditText: EditText
    private lateinit var therapyLocationEditText: EditText
    private lateinit var therapistEditText: EditText
    private lateinit var addButton: Button

    // Store the therapy name
    private var therapyName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.therapy_session_new, container, false)

        // Initialize EditTexts and Button
        therapyDateEditText = view.findViewById(R.id.therapyDate)
        therapyTimeEditText = view.findViewById(R.id.therapyTime)
        therapyLocationEditText = view.findViewById(R.id.therapyLocation)
        therapistEditText = view.findViewById(R.id.therapist)
        addButton = view.findViewById(R.id.addButton)

        // Get the therapy name from arguments
        therapyName = arguments?.getString("therapyName")

        // Set OnClickListener for the therapyDate EditText to show DatePickerDialog
        therapyDateEditText.setOnClickListener {
            showDatePicker()
        }

        // Set OnClickListener for the therapyTime EditText to show TimePickerDialog
        therapyTimeEditText.setOnClickListener {
            showTimePicker()
        }

        // Set OnClickListener for the Add button to save the data to Firebase
        addButton.setOnClickListener {
            saveTherapySession()
        }

        return view
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Set selected date in the therapyDate EditText
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                therapyDateEditText.setText(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                // Convert 24-hour format to 12-hour format with AM/PM
                val isPM = selectedHour >= 12
                val formattedHour = if (selectedHour % 12 == 0) 12 else selectedHour % 12
                val amPm = if (isPM) "PM" else "AM"

                // Format the time as HH:MM AM/PM
                val formattedTime = String.format("%02d:%02d %s", formattedHour, selectedMinute, amPm)
                therapyTimeEditText.setText(formattedTime)
            },
            hour, minute, false  // Set to false for 12-hour format
        )
        timePickerDialog.show()
    }

    private fun saveTherapySession() {
        val date = therapyDateEditText.text.toString()
        val time = therapyTimeEditText.text.toString()
        val location = therapyLocationEditText.text.toString()
        val therapist = therapistEditText.text.toString()

        // Check if all fields are filled
        if (date.isEmpty() || time.isEmpty() || location.isEmpty() || therapist.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Save to Firebase Realtime Database with user ID
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid

        if (userId != null) {
            val database = FirebaseDatabase.getInstance().reference
            val therapySessionRef = database.child("therapy_sessions").push() // Push creates a unique key

            // Add status and notes with default values
            val therapySessionData = mapOf(
                "userId" to userId,  // Add userId as an attribute
                "date" to date,
                "time" to time,
                "location" to location,
                "therapist" to therapist,
                "status" to "upcoming",  // Default value
                "notes" to "" ,           // Empty for now
                "therapyName" to therapyName
            )

            therapySessionRef.setValue(therapySessionData).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(requireContext(), "Therapy session added successfully", Toast.LENGTH_SHORT).show()
                    dismiss()  // Close the dialog
                } else {
                    Toast.makeText(requireContext(), "Failed to add therapy session", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    companion object {
        fun newInstance(therapyName: String): TherapySessionDialog {
            val dialog = TherapySessionDialog()
            val args = Bundle()
            args.putString("therapyName", therapyName)
            dialog.arguments = args
            return dialog
        }
    }
}
