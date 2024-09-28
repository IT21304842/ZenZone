package com.example.zenzoneapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment

class MedicationDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.medication_new, container, false)

        view.findViewById<Button>(R.id.addButton).setOnClickListener {
            // Handle add medication logic here
            dismiss() // Close the dialog
        }

        return view
    }
}
