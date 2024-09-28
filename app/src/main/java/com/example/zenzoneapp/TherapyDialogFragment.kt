package com.example.zenzoneapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

class TherapyDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this dialog
        return inflater.inflate(R.layout.therapy_new, container, false)
    }

    // Handle dialog actions (e.g., button clicks) here if necessary
}

