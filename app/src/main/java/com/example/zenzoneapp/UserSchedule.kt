package com.example.zenzoneapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UserSchedule : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserScheduleAdapter
    private lateinit var scheduleList: List<UserScheduleData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_schedule, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Dummy data
        scheduleList = listOf(
            UserScheduleData("Activity 1", "Description for Activity 1"),
            UserScheduleData("Activity 2", "Description for Activity 2"),
            UserScheduleData("Activity 3", "Description for Activity 3")
        )

        // Initialize the adapter with edit and delete actions
        adapter = UserScheduleAdapter(
            requireContext(),
            scheduleList,
            onEditClick = { scheduleData ->
                // Handle edit action
                editSchedule(scheduleData)
            },
            onDeleteClick = { scheduleData ->
                // Handle delete action
                deleteSchedule(scheduleData)
            }
        )

        // Set the adapter to RecyclerView
        recyclerView.adapter = adapter

        return view
    }

    // Method to handle edit schedule
    private fun editSchedule(scheduleData: UserScheduleData) {
        // Implement the action to edit the selected schedule
        // For example: Navigate to an edit fragment or show a dialog
    }

    private fun deleteSchedule(scheduleData: UserScheduleData) {
        // Remove from the list and notify adapter
        scheduleList = scheduleList.filter { it != scheduleData }
        adapter.notifyDataSetChanged() // or use notifyItemRemoved if you manage the list with mutable types
    }

}
