package com.example.zenzoneapp

import MedicationListAdapter
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager

class Therapy : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var therapyAdapter: TherapyListAdapter
    private lateinit var medicationRecyclerView: RecyclerView
    private lateinit var medicationAdapter: MedicationListAdapter
    private var medicationList = listOf(
        Medication("Medication 1", "10mg"),
        Medication("Medication 2", "5mg"),
        Medication("Medication 3", "20mg")
    )

    private val therapyList = listOf(
        TherapyItem("Therapy 1", R.drawable.t1),
        TherapyItem("Therapy 2", R.drawable.t2),
        TherapyItem("Therapy 3", R.drawable.t3),
        TherapyItem("Therapy 4", R.drawable.t4),
        TherapyItem("Therapy 5", R.drawable.t5),
        TherapyItem("Therapy 6", R.drawable.t6)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_therapy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up Therapy RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewTherapies)
        therapyAdapter = TherapyListAdapter(therapyList) { selectedTherapy ->
            // Navigate to TherapyView fragment
            val fragment = TherapyView.newInstance(selectedTherapy.name)
            val fragmentManager = (requireActivity() as AppCompatActivity).supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment) // Ensure this ID matches your main fragment container
                .addToBackStack(null) // Add to back stack for back navigation
                .commit()
        }
        val gridLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = therapyAdapter

        // Set up Medication RecyclerView
        medicationRecyclerView = view.findViewById(R.id.recyclerViewMedications)
        medicationAdapter = MedicationListAdapter(medicationList)
        medicationRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        medicationRecyclerView.adapter = medicationAdapter
    }


}



