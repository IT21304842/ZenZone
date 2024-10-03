package com.example.zenzoneapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class HistorySchedule : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var historyScheduleAdapter: HistoryScheduleAdapter
    private val historyScheduleList = mutableListOf<ActivityData>()

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_history_schedule, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize the adapter with data
        historyScheduleAdapter = HistoryScheduleAdapter(historyScheduleList) { historySchedule ->
            // Handle comment click event
            val index = historyScheduleList.indexOf(historySchedule)
            if (index != -1) {
                historyScheduleList[index] = historySchedule.copy(comment = "New comment added!")
                historyScheduleAdapter.notifyItemChanged(index)
            }
        }

        recyclerView.adapter = historyScheduleAdapter

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Load history schedule data
        loadHistoryScheduleData()

        return view
    }

    private fun loadHistoryScheduleData() {
        val userId = auth.currentUser?.uid ?: return

        // Get today's date in the format used in the database (yyyy-MM-d)
        val today = SimpleDateFormat("yyyy-MM-d", Locale.getDefault()).format(Date())

        // Log today's date for debugging
        Log.d("HistorySchedule", "Today's date: $today")

        // Query the database for activities of the user
        database.child("activities").orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    historyScheduleList.clear() // Clear the list before adding new data
                    for (data in snapshot.children) {
                        val activity = data.getValue(ActivityData::class.java)

                        if (activity != null) {
                            Log.d("HistorySchedule", "Retrieved activity: $activity")

                            // Normalize the activity date format for comparison
                            val activityDate = activity.date
                            if (activityDate < today) { // Check if activity date is earlier than today
                                historyScheduleList.add(activity)
                                Log.d("HistorySchedule", "Activity added: $activity")
                            }
                        }
                    }
                    historyScheduleAdapter.notifyDataSetChanged() // Notify the adapter of the data change
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("HistorySchedule", "Database error: ${error.message}")
                }
            })
    }


}
