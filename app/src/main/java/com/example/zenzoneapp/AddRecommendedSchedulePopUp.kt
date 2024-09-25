package com.example.zenzoneapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class AddRecommendedSchedulePopUp(
    private val activityName: String,
    private val activityDescription: String
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.schedule_recommended, container, false)

        // Set the activity name and description
        val activityNameTextView = view.findViewById<TextView>(R.id.textView10)
        val activityDescriptionTextView = view.findViewById<TextView>(R.id.desc)
        val specialNotesEditText = view.findViewById<EditText>(R.id.specialNotesEditText)
        val addButton = view.findViewById<Button>(R.id.addButton)

        activityNameTextView.text = activityName
        activityDescriptionTextView.text = activityDescription

        // Handle Add Activity button click in dialog
        addButton.setOnClickListener {
            val specialNotes = specialNotesEditText.text.toString()
            // Handle the action to save the activity along with special notes
            dismiss() // Close the dialog after adding the activity
        }

        // Close button to dismiss dialog
        view.findViewById<View>(R.id.txtclose).setOnClickListener {
            dismiss()
        }

        return view
    }
}
