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
        val editButton: Button = dialog.findViewById(R.id.editButton)

        // Set initial values
        editTextName.setText(scheduleData.activityName)

        // Confirm edit
        editButton.setOnClickListener {
            val updatedSchedule = ActivityData(editTextName.text.toString(), scheduleData.description)
            onEditConfirmed(updatedSchedule)
            dismiss()
        }

        return dialog
    }
}
