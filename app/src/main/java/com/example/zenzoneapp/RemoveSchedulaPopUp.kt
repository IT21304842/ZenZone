package com.example.zenzoneapp

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class RemoveSchedulaPopUp(private val scheduleData: UserScheduleData, private val onRemoveConfirmed: (UserScheduleData) -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.remove_schedule_popup)

        // Set dialog views
        val txtClose: ImageView = dialog.findViewById(R.id.txtclose)
        val desc: TextView = dialog.findViewById(R.id.desc)
        val removeButton: Button = dialog.findViewById(R.id.addButton)

        desc.text = "Do you want to remove ${scheduleData.name} from Schedule?"

        // Close dialog
        txtClose.setOnClickListener { dismiss() }

        // Confirm removal
        removeButton.setOnClickListener {
            onRemoveConfirmed(scheduleData)
            dismiss()
        }

        return dialog
    }
}
