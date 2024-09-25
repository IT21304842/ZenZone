package com.example.zenzoneapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecommendationSchedule : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecommendedScheduleAdapter
    private lateinit var recommendedList: List<RecommendedScheduleData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recommendation_schedule, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Dummy data for recommended schedules
        recommendedList = listOf(
            RecommendedScheduleData("Recommended Activity 1", "Description for Activity 1"),
            RecommendedScheduleData("Recommended Activity 2", "Description for Activity 2"),
            RecommendedScheduleData("Recommended Activity 3", "Description for Activity 3")
        )

        // Initialize the adapter with Add Activity click action
        adapter = RecommendedScheduleAdapter(
            requireContext(),
            recommendedList,
            onAddClick = { recommendedSchedule ->
                // Handle Add Activity action
                addActivity(recommendedSchedule)
            }
        )

        // Set the adapter to RecyclerView
        recyclerView.adapter = adapter

        return view
    }

    // Method to handle adding the activity to the user's schedule
    private fun addActivity(recommendedSchedule: RecommendedScheduleData) {
        // Implement the action to add the recommended activity
        // You can save it to the user's schedule or show a confirmation message
    }
}
