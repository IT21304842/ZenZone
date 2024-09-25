package com.example.zenzoneapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistorySchedule : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var historyScheduleAdapter: HistoryScheduleAdapter
    private val historyScheduleList = mutableListOf<HistoryScheduleData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_history_schedule, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize the adapter with data and comment click handler
        historyScheduleAdapter = HistoryScheduleAdapter(historyScheduleList) { historySchedule ->
            // Handle comment click event, you can open a dialog or a new screen for adding a comment
            // For now, we'll just update the comment as a demonstration
            val index = historyScheduleList.indexOf(historySchedule)
            if (index != -1) {
                historyScheduleList[index] = historySchedule.copy(comment = "New comment added!")
                historyScheduleAdapter.notifyItemChanged(index)
            }
        }

        recyclerView.adapter = historyScheduleAdapter

        // Load some demo data (you can load real data from a database or API)
        loadHistoryScheduleData()

        return view
    }

    private fun loadHistoryScheduleData() {
        historyScheduleList.add(HistoryScheduleData("Activity 1", "Description 1", ""))
        historyScheduleList.add(HistoryScheduleData("Activity 2", "Description 2", ""))
        historyScheduleList.add(HistoryScheduleData("Activity 3", "Description 3", ""))
        historyScheduleAdapter.notifyDataSetChanged()
    }
}
