package com.example.zenzoneapp

import MedicationListAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.Query

class Therapy : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var therapyAdapter: TherapyListAdapter
    private lateinit var medicationRecyclerView: RecyclerView

    private lateinit var medicationAdapter: MedicationListAdapter
    private var medicationList = mutableListOf<Medication>()


    private val therapyList = mutableListOf<TherapyItem>()

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_therapy, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Get the logged-in user's userId
        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("therapies")

        // Set up Therapy RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewTherapies)
        therapyAdapter = TherapyListAdapter(therapyList) { selectedTherapy ->
            // Navigate to TherapyView fragment
            val fragment = TherapyView.newInstance(selectedTherapy.acronym)
            val fragmentManager = (requireActivity() as AppCompatActivity).supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit()
        }
        val gridLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = therapyAdapter

        // Fetch therapy data from Firebase
        fetchTherapyData()

        // Fetch medication data based on userId
        if (userId != null) {
            fetchMedicationData(userId)
        }

        // Set up Medication RecyclerView
        medicationRecyclerView = view.findViewById(R.id.recyclerViewMedications)
        medicationAdapter = MedicationListAdapter(medicationList)
        medicationRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        medicationRecyclerView.adapter = medicationAdapter

        // Set up click listener for addMedication button
        view.findViewById<FloatingActionButton>(R.id.addMed).setOnClickListener {
            val medicationDialog = MedicationDialogFragment()
            medicationDialog.show(parentFragmentManager, "MedicationDialog")
        }

        // Set up click listener for addTherapy button
        view.findViewById<FloatingActionButton>(R.id.addTherapy).setOnClickListener {
            val therapyDialog = TherapyDialogFragment()
            therapyDialog.show(parentFragmentManager, "TherapyDialog")
        }
    }

    private fun fetchTherapyData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                therapyList.clear()
                for (therapySnapshot in snapshot.children) {
                    val therapy = therapySnapshot.getValue<TherapyItem>()
                    therapy?.let { therapyList.add(it) }
                }
                therapyAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Therapy", "Error fetching data: ${error.message}")
            }
        })
    }

    private fun fetchMedicationData(userId: String) {
        // Reference to medication data without userId as the parent node
        val medicationRef: Query = FirebaseDatabase.getInstance()
            .getReference("medications")
            .orderByChild("userId")
            .equalTo(userId)

        medicationRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                medicationList.clear()
                for (medicationSnapshot in snapshot.children) {
                    val medication = medicationSnapshot.getValue<Medication>()
                    Log.d("Medications", "Fetched medication: $medication") // Add this log
                    medication?.let { medicationList.add(it) }
                }
                medicationAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Therapy", "Error fetching medication data: ${error.message}")
            }
        })
    }

}



