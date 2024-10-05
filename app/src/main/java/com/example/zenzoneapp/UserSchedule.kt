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
import java.util.Calendar
import java.util.Date
import java.util.Locale

class UserSchedule : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserScheduleAdapter
    private lateinit var scheduleList: MutableList<UserScheduleData>
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_schedule, container, false)

        // Initialize Firebase Auth and Database Reference
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        scheduleList = mutableListOf()
        adapter = UserScheduleAdapter(this, scheduleList, ::onEditClick, ::onDeleteClick) // Pass 'this' as the UserSchedule instance
        recyclerView.adapter = adapter

        // Load today's activities by default
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val today = getTodayDate()
            loadScheduleData(userId, today) // Load today's date by default
        } else {
            Log.e("UserSchedule", "User ID is null, cannot load schedule data.")
        }

        return view
    }

    fun updateSelectedDate(selectedDate: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            loadScheduleData(userId, selectedDate)
        } else {
            Log.e("UserSchedule", "User ID is null, cannot load schedule data.")
        }
    }

    fun loadScheduleData(userId: String, selectedDate: String) {
        // Check for database initialization
        if (!::database.isInitialized) {
            Log.e("UserSchedule", "Database reference has not been initialized.")
            return
        }

        database.child("activities").orderByChild("userId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    scheduleList.clear()
                    for (dataSnapshot in snapshot.children) {
                        val activityData = dataSnapshot.getValue(UserScheduleData::class.java)
                        if (activityData != null && activityData.date == selectedDate) {
                            scheduleList.add(activityData)
                        }
                    }
                    adapter.updateScheduleList(scheduleList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("UserSchedule", "Database error: ${error.message}")
                }
            })
    }

    fun getTodayDate(): String {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH is zero-based
        val year = calendar.get(Calendar.YEAR)
        return String.format("%04d-%d-%d", year, month, day) // Change "%02d" to "%d" for day
    }

    private fun onEditClick(scheduleData: UserScheduleData) {
        // Handle edit action
    }

    private fun onDeleteClick(scheduleData: UserScheduleData) {
        // Handle delete action
    }

    // New method to remove an activity
    fun removeActivity(activityId: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child("activities").child(activityId).removeValue()
                .addOnSuccessListener {
                    Log.d("UserSchedule", "Activity removed successfully.")
                    // Optionally, you can reload the schedule or update the UI here
                    loadScheduleData(userId, getTodayDate()) // Reload the schedule
                }
                .addOnFailureListener { error ->
                    Log.e("UserSchedule", "Failed to remove activity: ${error.message}")
                }
        } else {
            Log.e("UserSchedule", "User ID is null, cannot remove activity.")
        }
    }
}
