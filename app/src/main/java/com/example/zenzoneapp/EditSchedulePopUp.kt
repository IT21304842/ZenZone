package com.example.zenzoneapp

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment

class EditSchedulePopUp(private val scheduleData: ActivityData, private val onEditConfirmed: (ActivityData) -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.edit_schedule_popup)

        // Set dialog views
        val editTextName: EditText = dialog.findViewById(R.id.editTextName)
        val editTextDescription: EditText = dialog.findViewById(R.id.descriptionEditText)
        val editTextNotes: EditText = dialog.findViewById(R.id.specialNotesEditText)
        val editButton: Button = dialog.findViewById(R.id.editButton)

        // Set initial values from existing data
        editTextName.setText(scheduleData.activityName)
        editTextDescription.setText(scheduleData.description)
        editTextNotes.setText(scheduleData.specialNotes)

        // Confirm edit
        editButton.setOnClickListener {
            val updatedSchedule = ActivityData(
                activityId = scheduleData.activityId,
                activityName = editTextName.text.toString(),
                description = editTextDescription.text.toString(),
                specialNotes = editTextNotes.text.toString(),
                userId = scheduleData.userId,
                date = scheduleData.date
            )
            onEditConfirmed(updatedSchedule)
            dismiss()
        }

        return dialog
    }
}
